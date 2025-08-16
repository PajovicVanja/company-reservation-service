package org.soa.reservation_service.service;

import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.SmsFaasClient;
import org.soa.reservation_service.model.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Handles reservation-related SMS messages via sms-faas.
 */
@Service
public class ReservationNotificationService {

    private static final Logger log = Logger.getLogger(ReservationNotificationService.class.getName());

    private final SmsFaasClient smsFaasClient;

    @Value("${SMS_DEFAULT_PHONE:+38640111222}")
    private String defaultPhone;

    @Value("${SMS_CONFIRM_TEMPLATE_ID:}")
    private String confirmTemplateId; // optional template for confirmation

    public ReservationNotificationService(SmsFaasClient smsFaasClient) {
        this.smsFaasClient = smsFaasClient;
    }

    /** Immediate confirmation at reservation creation. */
    public void sendReservationCreated(Reservation reservation) {
        try {
            String phone = resolvePhone(reservation);
            String msg = buildConfirmationMessage(reservation);

            if (confirmTemplateId != null && !confirmTemplateId.isBlank()) {
                var vars = new java.util.HashMap<String, String>();
                vars.put("name", safe(reservation.getCustomerFullName(), "there"));
                vars.put("service", safe(reservation.getService() != null ? reservation.getService().getName() : null, "your appointment"));
                vars.put("time", formatWhen(reservation.getDate().toLocalDateTime()));
                vars.put("company", safe(reservation.getCompany() != null ? reservation.getCompany().getCompanyName() : null, "our company"));
                smsFaasClient.sendSms(phone, confirmTemplateId, null, vars);
            } else {
                smsFaasClient.sendSms(phone, null, msg, null);
            }

            log.info("[SMS] Confirmation sent to " + phone + " for reservation id=" + reservation.getId());
        } catch (Exception e) {
            log.warning("[SMS] Failed to send confirmation: " + e.getMessage());
        }
    }

    /** Reminder based on SmsNotificationConfig (day-before, hour-before, etc). */
    public void sendReminder(Reservation reservation, SmsNotificationConfig cfg) {
        try {
            String phone = resolvePhone(reservation);
            String text;

            if (cfg != null && cfg.getNotificationMessage() != null && !cfg.getNotificationMessage().isBlank()) {
                text = cfg.getNotificationMessage();
            } else {
                // Default reminder format
                String serviceName = safe(reservation.getService() != null ? reservation.getService().getName() : null, "your appointment");
                String when = formatWhen(reservation.getDate().toLocalDateTime());
                String company = safe(reservation.getCompany() != null ? reservation.getCompany().getCompanyName() : null, "our company");
                text = "Reminder: " + serviceName + " at " + when + " with " + company + ".";
            }

            smsFaasClient.sendSms(phone, null, text, null);
            log.info("[SMS] Reminder sent to " + phone + " for reservation id=" + reservation.getId());
        } catch (Exception e) {
            log.warning("[SMS] Failed to send reminder: " + e.getMessage());
            throw e; // let caller decide whether to mark sent
        }
    }

    private String resolvePhone(Reservation reservation) {
        if (reservation.getCustomerPhoneNumber() != null && reservation.getCustomerPhoneNumber() > 0) {
            return String.valueOf(reservation.getCustomerPhoneNumber());
        }
        return defaultPhone;
    }

    private String buildConfirmationMessage(Reservation r) {
        String when = formatWhen(r.getDate().toLocalDateTime());
        String serviceName = safe(r.getService() != null ? r.getService().getName() : null, "your appointment");
        String customer = safe(r.getCustomerFullName(), "there");
        String company = safe(r.getCompany() != null ? r.getCompany().getCompanyName() : null, "our company");
        return "Hi " + customer + ", your " + serviceName + " at " + when + " with " + company + " is confirmed.";
    }

    private static String formatWhen(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // adjust format to your locale if desired
    }

    private static String safe(String s, String fallback) {
        return (s != null && !s.isBlank()) ? s : fallback;
    }
}
