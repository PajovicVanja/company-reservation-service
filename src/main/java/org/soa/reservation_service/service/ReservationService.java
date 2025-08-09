// src/main/java/org/soa/reservation_service/service/ReservationService.java
package org.soa.reservation_service.service;

import org.soa.companyService.model.BusinessHours;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceM;
import org.soa.companyService.service.BusinessHoursService;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceService;
import org.soa.reservation_service.client.EmployeeClient;
import org.soa.reservation_service.dto.ReservationCreateRequest;
import org.soa.reservation_service.dto.TimeInterval;
import org.soa.reservation_service.model.Reservation;
import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.model.Status;
import org.soa.reservation_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private StatusService statusService;
    @Autowired private BusinessHoursService businessHoursService;
    @Autowired private ServiceService serviceService;
    @Autowired private CompanyService companyService;
    @Autowired private PaymentService paymentService;
    @Autowired private ScheduledNotificationsService scheduledNotificationsService;

    // New: client for employee-service (REST)
    @Autowired private EmployeeClient employeeClient;

    // Get all reservations
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
                LocalDateTime.now().plusHours(1));
    }

    // Get a reservation by ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Create a new reservation
    public Reservation createReservation(ReservationCreateRequest reservation) {
        Reservation newReservation = new Reservation();
        newReservation.setDate(reservation.getDate());
        newReservation.setIdCustomer(reservation.getIdCustomer());
        newReservation.setTwoFACode((long) new Random().nextInt(100000, 999999));
        newReservation.setNotified(false);
        newReservation.setHidden(false);
        newReservation.setCustomerEmail(reservation.getCustomerEmail());
        newReservation.setCustomerPhoneNumber(reservation.getCustomerPhoneNumber());
        newReservation.setCustomerFullName(reservation.getCustomerFullName());

        Company company = reservation.getIdCompany() != null
                ? companyService.getCompanyById(reservation.getIdCompany())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + reservation.getIdCompany()))
                : null;
        newReservation.setCompany(company);

        ServiceM service = serviceService.getServiceById(reservation.getIdService())
                .orElseThrow(() -> new RuntimeException("Service not found with id " + reservation.getIdService()));
        newReservation.setService(service);

        newReservation.setDateTo(
                Timestamp.valueOf(
                        reservation.getDate().toLocalDateTime().plusMinutes(service.getDuration())
                )
        );

        newReservation.setStatus(statusService.getDefaultStatus());

        newReservation.setPayment(
                paymentService.getPaymentById(reservation.getPaymentId())
                        .orElseThrow(() -> new RuntimeException("Payment not found with id " + reservation.getPaymentId()))
        );

        // Set employee id (no JPA relation)
        newReservation.setEmployeeId(reservation.getEmployeeId());

        // Optional validation against employee-service
        if (reservation.getEmployeeId() != null) {
            employeeClient.assertEmployeeActive(reservation.getEmployeeId());
            employeeClient.assertEmployeeHasSkill(reservation.getEmployeeId(), reservation.getIdService());
            employeeClient.assertEmployeeAvailable(
                    reservation.getEmployeeId(),
                    reservation.getDate().toLocalDateTime(),
                    (short) service.getDuration()
            );
        }

        newReservation.setSendSms(Timestamp.valueOf(calculateSmsNotificationTime(newReservation.getDate(), newReservation)));
        return reservationRepository.save(newReservation);
    }

    // problem sa vise konfuguracija ukljucenih
    public LocalDateTime calculateSmsNotificationTime(Timestamp date, Reservation reservation) {
        ScheduledNotifications notification = new ScheduledNotifications();
        notification.setReservation(reservation);
        notification.setNotificationType(reservation.getCompany().getSmsNotificationConfig());
        notification.setSent(false);

        switch (reservation.getCompany().getSmsNotificationConfig().getName()) {
            case "ENA URA PREJ":
                notification.setScheduledTime(date.toLocalDateTime().minusHours(1));
                break;
            case "EN DAN PREJ":
                notification.setScheduledTime(date.toLocalDateTime().minusHours(24));
                break;
            default:
                throw new RuntimeException("Invalid SMS notification configuration");
        }

        scheduledNotificationsService.createScheduledNotification(notification);
        return notification.getScheduledTime();
    }

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

    /**
     * Calculate free slots for a company.
     * Signature already supports employeeId for future intersection with employee availability.
     * Current version keeps business-hours logic only (intersection can be added later).
     */
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

        // Build free intervals from business hours minus busy intervals
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

        // Slice free intervals into slots of service duration
        List<TimeInterval> availableSlots = new ArrayList<>();
        for (TimeInterval free : freeIntervals) {
            LocalTime slotStart = free.start;
            while (!slotStart.plus(serviceDuration).isAfter(free.end)) {
                LocalTime slotEnd = slotStart.plus(serviceDuration);
                availableSlots.add(new TimeInterval(slotStart, slotEnd));
                slotStart = slotEnd;
            }
        }

        // TODO (optional): if employeeId != null, intersect with employee availability via employeeClient.getAvailabilityWindows(...)

        return availableSlots.stream()
                .map(slot -> slot.start + " - " + slot.end)
                .toList();
    }

    private List<TimeInterval> getBusyHours(List<Reservation> listOfUsedTermins, BusinessHours businessHour) {
        List<TimeInterval> busySlots = new ArrayList<>();
        // lunch/pause
        busySlots.add(new TimeInterval(
                businessHour.getPauseFrom().toLocalTime(),
                businessHour.getPauseTo().toLocalTime()
        ));
        for (Reservation reservation : listOfUsedTermins) {
            busySlots.add(new TimeInterval(
                    reservation.getDate().toLocalDateTime().toLocalTime(),
                    reservation.getDateTo().toLocalDateTime().toLocalTime()
            ));
        }
        return busySlots;
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
}
