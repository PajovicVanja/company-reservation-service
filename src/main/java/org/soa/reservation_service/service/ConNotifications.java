package org.soa.reservation_service.service;

import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class ConNotifications {

    private static final Logger logger = Logger.getLogger(ConNotifications.class.getName());

    @Autowired private ScheduledNotificationsService scheduledNotificationsService;
    @Autowired private ReservationNotificationService reservationNotificationService;
    @Autowired private ReservationRepository reservationRepository;

    /**
     * Poll due notifications and send reminders.
     * Defaults to every 60s; override with notifications.poll.ms
     */
    @Scheduled(fixedDelayString = "${notifications.poll.ms:60000}", initialDelayString = "${notifications.initialDelay.ms:10000}")
    public void processDueNotifications() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<ScheduledNotifications> due = scheduledNotificationsService.findDue(now);
            if (due.isEmpty()) {
                return;
            }

            int ok = 0, fail = 0;
            for (ScheduledNotifications sn : due) {
                try {
                    var reservation = sn.getReservation();
                    reservationNotificationService.sendReminder(reservation, sn.getNotificationType());

                    // mark notification as sent
                    sn.setSent(true);
                    scheduledNotificationsService.createScheduledNotification(sn);

                    // mark reservation as notified (if not already)
                    if (reservation != null && !reservation.isNotified()) {
                        reservation.setNotified(true);
                        reservationRepository.save(reservation);
                    }
                    ok++;
                } catch (Exception ex) {
                    fail++;
                    logger.warning("Reminder send failed for scheduled_notification id=" + sn.getId() + ": " + ex.getMessage());
                }
            }
            logger.info("Processed reminders: ok=" + ok + ", fail=" + fail);
        } catch (Exception ex) {
            logger.warning("processDueNotifications failed: " + ex.getMessage());
        }
    }
}
