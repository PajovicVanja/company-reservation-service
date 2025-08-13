package org.soa.reservation_service.service;

import com.google.gson.Gson;
import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class ConNotifications {

    private static final Logger logger = Logger.getLogger(ConNotifications.class.getName());

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Runs at minute 0 of every hour.
     * NOTE: Scheduled methods must be NO-ARG.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void sendSmsNotificationOneHourBefore() {
        try {
            // TODO: Replace with real query for notifications one hour before
            // reservationService.getAllReservationsForNotify().forEach(reservation -> {
            //     // Build/find ScheduledNotifications and call sendSms(...)
            // });
            logger.info("Hourly notification job executed.");
        } catch (Exception ex) {
            logger.warning("Hourly notification job failed: " + ex.getMessage());
        }
    }

    /**
     * Runs every day at 08:00.
     * NOTE: Scheduled methods must be NO-ARG.
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void sendSmsNotificationOneDayBefore() {
        try {
            // TODO: Implement daily notification lookup and sending
            logger.info("Daily 08:00 notification job executed.");
        } catch (Exception ex) {
            logger.warning("Daily notification job failed: " + ex.getMessage());
        }
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
                .header("Authorization", System.getenv("SMS_API_KEY"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(params)))
                .build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            logger.warning("Failed to send SMS: IOException: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            logger.warning("Failed to send SMS: InterruptedException: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
