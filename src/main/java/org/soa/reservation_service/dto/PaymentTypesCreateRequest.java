package org.soa.reservation_service.dto;

import java.sql.Timestamp;

public class PaymentTypesCreateRequest {

    private String name;
    private Timestamp durationFrom;
    private Timestamp durationTo;
    private Long addedBy;
    private Long deletedBy;

    // Getters and Setters
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
