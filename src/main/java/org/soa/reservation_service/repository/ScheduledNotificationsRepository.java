package org.soa.reservation_service.repository;

import org.soa.reservation_service.model.ScheduledNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledNotificationsRepository extends JpaRepository<ScheduledNotifications, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find notifications by status or date range

    // Example:
    // List<ScheduledNotification> findByStatus(String status);
    // List<ScheduledNotification> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
