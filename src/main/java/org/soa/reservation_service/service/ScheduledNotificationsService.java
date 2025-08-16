package org.soa.reservation_service.service;

import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.repository.ScheduledNotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledNotificationsService {

    @Autowired
    private ScheduledNotificationsRepository scheduledNotificationsRepository;

    public ScheduledNotifications createScheduledNotification(ScheduledNotifications scheduledNotification) {
        // save() works for both create and update
        return scheduledNotificationsRepository.save(scheduledNotification);
    }

    public List<ScheduledNotifications> findDue(LocalDateTime now) {
        return scheduledNotificationsRepository.findTop200BySentFalseAndScheduledTimeLessThanEqual(now);
    }
}
