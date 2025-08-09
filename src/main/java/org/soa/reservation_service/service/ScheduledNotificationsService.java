package org.soa.reservation_service.service;

import org.soa.reservation_service.model.ScheduledNotifications;
import org.soa.reservation_service.repository.ScheduledNotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduledNotificationsService {

    @Autowired
    private ScheduledNotificationsRepository scheduledNotificationsRepository;

    public ScheduledNotifications createScheduledNotification(ScheduledNotifications scheduledNotification) {
         return scheduledNotificationsRepository.save(scheduledNotification);
    }
}
