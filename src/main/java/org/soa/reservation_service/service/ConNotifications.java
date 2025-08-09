package org.soa.reservation_service.service;

import com.google.gson.Gson;
import org.soa.companyService.model.Company;
import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ConNotifications {

    Logger logger = Logger.getLogger(ConNotifications.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 * * * *") // This cron expression means every 5 minutes
    public void sendSmsNotificationOneHourBefore(String message) {
        // This method would contain the logic to send a notification
        // For example, it could use an email service, SMS service, or push notification service

        // get all scheduled notifications with notification type "ONE_HOUR_BEFORE"
        // and not sent and not cancelled and date is one hour before the scheduled time

        // treba da ide iz schedule-a
//        reservationService.getAllReservationsForNotify().forEach(reservation -> {
//            String notificationMessage = "Reservation ID: " + reservation.getId() + " is due for notification.";
//            reservation.setNotified(true);
//            reservationRepository.save(reservation);
//        });

        System.out.println("Notification sent: " + message);
    }

    @Scheduled(cron = "0 0 8 * * *") // This cron expression means every day at 9 AM
    public void sendSmsNotificationOneDayBefore(ScheduledNotifications scheduledNotifications) {
        // get all scheduled notifications with notification type "ONE_DAY_BEFORE" or "TWO_DAYS_BEFORE"
        // and not sent and not cancelled and date is one day before the scheduled time
    }

    private void sendSms(ScheduledNotifications scheduledNotifications) {
        String link = "https://mbg.t-2.com/SMS/futuretech/send_sms";
        Map<String, String> params = new HashMap<>();
        params.put("from_number", "EasyBooksy");
        params.put("to_number", String.valueOf(scheduledNotifications.getReservation().getCustomerPhoneNumber()));
        params.put("message", scheduledNotifications.getNotificationType().getNotificationMessage());

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(link))
                .header("Authorization", System.getenv("SMS_API_KEY")) // Assuming you have an API key stored in an environment variable
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(params)))
                .build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            logger.warning("Failed to send SMS: IOException :" + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            logger.warning("Failed to send SMS: InterruptedException : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
