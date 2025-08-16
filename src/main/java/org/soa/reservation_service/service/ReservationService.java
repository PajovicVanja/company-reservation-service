package org.soa.reservation_service.service;

import org.soa.companyService.model.BusinessHours;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceM;
import org.soa.companyService.service.BusinessHoursService;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceService;
import org.soa.reservation_service.client.DiscountClient;
import org.soa.reservation_service.client.EmployeeClient;
import org.soa.reservation_service.dto.ReservationCreateRequest;
import org.soa.reservation_service.dto.TimeInterval;
import org.soa.reservation_service.model.Reservation;
import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.model.Status;
import org.soa.reservation_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@Service
public class ReservationService {

    private static final Logger log = Logger.getLogger(ReservationService.class.getName());

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private StatusService statusService;
    @Autowired private BusinessHoursService businessHoursService;
    @Autowired private ServiceService serviceService;
    @Autowired private CompanyService companyService;
    @Autowired private PaymentService paymentService;
    @Autowired private ScheduledNotificationsService scheduledNotificationsService;
    @Autowired(required = false) private DiscountClient discountClient; // optional bean
    @Autowired private EmployeeClient employeeClient;
    @Autowired private ReservationNotificationService reservationNotificationService; // immediate SMS

    // IMPORTANT: Integer here to match Reservation.idCustomer (Integer)
    @Value("${CUSTOMER_FALLBACK_ID:0}")
    private Integer customerFallbackId;

    private Integer resolveCustomerId(ReservationCreateRequest req) {
        Integer fromReq = req.getIdCustomer();
        if (fromReq != null) return fromReq;
        return (customerFallbackId != null ? customerFallbackId : 0);
    }

    // ───────────────────────── Queries ─────────────────────────

    public List<Reservation> getAllReservationsByCompany(Long idCompany) {
        return reservationRepository.findByIdCompany(idCompany);
    }

    public List<Reservation> getAllReservationsByUser(Long userID) {
        return reservationRepository.findByUserId(userID);
    }

    public List<Reservation> getAllReservationsForNotify() {
        return reservationRepository.findByDateBetween(
                false,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1)
        );
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // ───────────────────────── Create ─────────────────────────

    /** Save the reservation, create a scheduled reminder timestamp, send immediate SMS, mark notified=true. */
    @Transactional
    public Reservation createReservation(ReservationCreateRequest reservation) {
        Reservation newReservation = new Reservation();

        // required/core fields
        newReservation.setDate(reservation.getDate());
        newReservation.setIdCustomer(resolveCustomerId(reservation)); // Integer ✔
        long sixDigits = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        newReservation.setTwoFACode(sixDigits);
        newReservation.setNotified(false); // will flip to true after immediate SMS
        newReservation.setHidden(false);

        // customer-facing fields
        newReservation.setCustomerEmail(reservation.getCustomerEmail());
        newReservation.setCustomerPhoneNumber(reservation.getCustomerPhoneNumber());
        newReservation.setCustomerFullName(reservation.getCustomerFullName());

        // company (optional)
        Company company = (reservation.getIdCompany() != null)
                ? companyService.getCompanyById(reservation.getIdCompany())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + reservation.getIdCompany()))
                : null;
        newReservation.setCompany(company);

        // service (required)
        ServiceM service = serviceService.getServiceById(reservation.getIdService())
                .orElseThrow(() -> new RuntimeException("Service not found with id " + reservation.getIdService()));
        newReservation.setService(service);

        // compute end time from service duration
        newReservation.setDateTo(
                Timestamp.valueOf(reservation.getDate().toLocalDateTime().plusMinutes(service.getDuration()))
        );

        // default status
        newReservation.setStatus(statusService.getDefaultStatus());

        // payment (required)
        newReservation.setPayment(
                paymentService.getPaymentById(reservation.getPaymentId())
                        .orElseThrow(() -> new RuntimeException("Payment not found with id " + reservation.getPaymentId()))
        );

        // employee checks (optional if employeeId missing)
        newReservation.setEmployeeId(reservation.getEmployeeId());
        if (reservation.getEmployeeId() != null) {
            employeeClient.assertEmployeeActive(reservation.getEmployeeId());
            employeeClient.assertEmployeeHasSkill(reservation.getEmployeeId(), reservation.getIdService());
            employeeClient.assertEmployeeAvailable(
                    reservation.getEmployeeId(),
                    reservation.getDate().toLocalDateTime(),
                    (short) service.getDuration()
            );
        }

        // Discounts only if both customer & company are present and a discount client exists
        Integer customerIdInt = newReservation.getIdCustomer(); // Integer ✔
        Long companyIdLong = reservation.getIdCompany();
        Integer companyIdInt = (companyIdLong != null ? companyIdLong.intValue() : null);
        Long serviceIdLong = reservation.getIdService(); // third param is Long ✔

        if (discountClient != null
                && customerIdInt != null && customerIdInt > 0
                && companyIdInt != null) {
            try {
                discountClient.addLoyaltyPoints(customerIdInt, companyIdInt, serviceIdLong);
            } catch (Exception e) {
                log.warning("[Discount] ignored error: " + e.getMessage());
            }
        }

        // 1) Persist reservation FIRST to ensure we have a non-null ID
        Reservation saved = reservationRepository.save(newReservation);

        // 2) Compute and store scheduled reminder time (e.g., one day/hour before)
        LocalDateTime scheduled = calculateSmsNotificationTime(saved.getDate(), saved);
        saved.setSendSms(Timestamp.valueOf(scheduled));
        saved = reservationRepository.save(saved);

        // 3) Send IMMEDIATE confirmation SMS and flip notified=true
        reservationNotificationService.sendReservationCreated(saved);
        saved.setNotified(true);
        saved = reservationRepository.save(saved);

        return saved;
    }

    // ───────────────────────── Scheduling ─────────────────────────

    /**
     * Decide the SMS scheduled time for future reminder.
     * Falls back to "1 hour before" if company or config is missing/unknown.
     */
    public LocalDateTime calculateSmsNotificationTime(Timestamp date, Reservation reservation) {
        LocalDateTime start = date.toLocalDateTime();
        LocalDateTime scheduledTime;

        var company = reservation.getCompany();
        var cfg = (company != null) ? company.getSmsNotificationConfig() : null;
        String cfgName = (cfg != null && cfg.getName() != null) ? cfg.getName().trim() : "";

        switch (cfgName) {
            case "ENA URA PREJ":
                scheduledTime = start.minusHours(1);
                break;
            case "EN DAN PREJ":
                scheduledTime = start.minusHours(24);
                break;
            default:
                // Safe default if misconfigured: one hour before
                scheduledTime = start.minusHours(1);
                break;
        }

        ScheduledNotifications notification = new ScheduledNotifications();
        notification.setReservation(reservation);
        if (cfg != null) {
            notification.setNotificationType(cfg);
        }
        notification.setScheduledTime(scheduledTime);
        notification.setSent(false);

        scheduledNotificationsService.createScheduledNotification(notification);
        return notification.getScheduledTime();
    }

    // ───────────────────────── Mutations ─────────────────────────

    public Reservation updateReservationStatus(Long id, Long statusId) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    Status status = statusService.getStatusById(statusId)
                            .orElseThrow(() -> new RuntimeException("Status not found with id " + statusId));
                    existingReservation.setStatus(status);
                    return reservationRepository.save(existingReservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
        Status canceledStatus = statusService.findStatusByName("Preklicano")
                .orElseThrow(() -> new RuntimeException("Status Preklicano not found"));
        reservation.setStatus(canceledStatus);
        reservationRepository.save(reservation);
    }

    public Reservation adminUpdateReservation(Long id, Long statusId) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    Status status = statusService.getStatusById(statusId)
                            .orElseThrow(() -> new RuntimeException("Status not found with id " + statusId));
                    existingReservation.setStatus(status);
                    existingReservation.setHidden(true);
                    return reservationRepository.save(existingReservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    public Reservation confirmReservation(Long id, Long twoFACode) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    Status status = statusService.getConfirmStatus();
                    if (!existingReservation.getTwoFACode().equals(twoFACode)) {
                        throw new RuntimeException("Invalid two-factor authentication code");
                    }
                    existingReservation.setStatus(status);
                    return reservationRepository.save(existingReservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    // ───────────────────────── Availability (free slots) ─────────────────────────

    public List<String> getFreeSlots(Long idCompany, LocalDateTime date, Long idService, Long employeeId) {
        Optional<ServiceM> serviceM = serviceService.getServiceById(idService);
        if (serviceM.isEmpty()) {
            throw new RuntimeException("Service not found with id " + idService);
        }

        var businessHoursList = businessHoursService.getBusinessHoursByCompanyId(idCompany);
        businessHoursList.removeIf(bh -> !bh.getDayNumber().equals(date.getDayOfWeek().getValue()));
        if (businessHoursList.isEmpty()) return List.of();

        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

        var listOfUsedTermins = reservationRepository.findByIdCompany(idCompany, startOfDay, endOfDay);
        listOfUsedTermins.removeIf(r -> r.getStatus().getName().equals("Preklicano"));

        var bh = businessHoursList.getFirst();
        var busyIntervals = getBusyHours(listOfUsedTermins, bh);
        busyIntervals.sort(Comparator.comparing(i -> i.start));

        // free intervals = BH minus busy
        List<TimeInterval> freeIntervals = new ArrayList<>();
        LocalTime current = bh.getTimeFrom().toLocalTime();
        for (TimeInterval busy : busyIntervals) {
            if (current.isBefore(busy.start)) freeIntervals.add(new TimeInterval(current, busy.start));
            if (current.isBefore(busy.end)) current = busy.end;
        }
        if (current.isBefore(bh.getTimeTo().toLocalTime())) {
            freeIntervals.add(new TimeInterval(current, bh.getTimeTo().toLocalTime()));
        }

        Duration serviceDuration = Duration.ofMinutes(serviceM.get().getDuration());

        // slice free intervals into service-sized slots
        List<TimeInterval> availableSlots = new ArrayList<>();
        for (TimeInterval free : freeIntervals) {
            LocalTime slotStart = free.start;
            while (!slotStart.plus(serviceDuration).isAfter(free.end)) {
                LocalTime slotEnd = slotStart.plus(serviceDuration);
                availableSlots.add(new TimeInterval(slotStart, slotEnd));
                slotStart = slotEnd;
            }
        }

        // TODO: if employeeId != null, intersect with employee availability via employeeClient

        return availableSlots.stream()
                .map(slot -> slot.start + " - " + slot.end)
                .toList();
    }

    private List<TimeInterval> getBusyHours(List<Reservation> listOfUsedTermins, BusinessHours businessHour) {
        List<TimeInterval> busySlots = new ArrayList<>();

        // lunch/pause
        if (businessHour.getPauseFrom() != null && businessHour.getPauseTo() != null) {
            busySlots.add(new TimeInterval(
                    businessHour.getPauseFrom().toLocalTime(),
                    businessHour.getPauseTo().toLocalTime()
            ));
        }

        for (Reservation reservation : listOfUsedTermins) {
            busySlots.add(new TimeInterval(
                    reservation.getDate().toLocalDateTime().toLocalTime(),
                    reservation.getDateTo().toLocalDateTime().toLocalTime()
            ));
        }
        return busySlots;
    }

    public List<Reservation> getReservationsByEmployee(Long employeeId) {
        return reservationRepository.findByEmployeeId(employeeId);
    }
}
