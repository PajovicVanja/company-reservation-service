package org.soa.companyService.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "sms_notification_config")
public class SmsNotificationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "name")
    private String name;

    @Column(name = "notification_message")
    private String notificationMessage;

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
