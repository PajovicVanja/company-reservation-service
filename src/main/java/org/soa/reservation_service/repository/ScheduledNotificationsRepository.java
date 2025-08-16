package org.soa.reservation_service.repository;

import org.soa.reservation_service.model.ScheduledNotifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationsRepository extends JpaRepository<ScheduledNotifications, Long> {

    // Fetch a bounded batch of due, unsent notifications
    List<ScheduledNotifications> findTop200BySentFalseAndScheduledTimeLessThanEqual(LocalDateTime time);
}
