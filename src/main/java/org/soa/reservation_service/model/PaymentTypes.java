package org.soa.reservation_service.model;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "payment_types")
public class PaymentTypes {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(columnDefinition = "TEXT")
        private String name;

        @Column(name = "duration_from")
        private Timestamp durationFrom;

        @Column(name = "duration_to")
        private Timestamp durationTo;

        @Column(name = "added_by")
        private Long addedBy;

        @Column(name = "deleted_by")
        private Long deletedBy;

        // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDurationFrom() {
        return durationFrom;
    }

    public void setDurationFrom(Timestamp durationFrom) {
        this.durationFrom = durationFrom;
    }

    public Timestamp getDurationTo() {
        return durationTo;
    }

    public void setDurationTo(Timestamp durationTo) {
        this.durationTo = durationTo;
    }

    public Long getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Long addedBy) {
        this.addedBy = addedBy;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }
}
