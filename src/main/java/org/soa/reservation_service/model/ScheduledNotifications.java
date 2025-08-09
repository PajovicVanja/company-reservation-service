package org.soa.reservation_service.model;

import jakarta.persistence.*;
import org.soa.companyService.model.SmsNotificationConfig;

import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_notifications")
public class ScheduledNotifications {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_notification_type", referencedColumnName = "id")
    private SmsNotificationConfig notificationType;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "sent")
    private boolean sent;

    @ManyToOne
    @JoinColumn(name = "id_reservation", referencedColumnName = "id")
    private Reservation reservation;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SmsNotificationConfig getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(SmsNotificationConfig notificationType) {
        this.notificationType = notificationType;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
