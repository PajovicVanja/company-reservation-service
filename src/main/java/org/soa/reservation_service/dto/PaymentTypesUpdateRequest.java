package org.soa.reservation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "Payload to update a payment type")
public class PaymentTypesUpdateRequest {

    @Schema(example = "Card", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(example = "2025-01-01T00:00:00.000+00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Timestamp durationFrom;

    @Schema(example = "2025-12-31T23:59:59.000+00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Timestamp durationTo;

    @Schema(example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long addedBy;

    @Schema(example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long deletedBy;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Timestamp getDurationFrom() { return durationFrom; }
    public void setDurationFrom(Timestamp durationFrom) { this.durationFrom = durationFrom; }
    public Timestamp getDurationTo() { return durationTo; }
    public void setDurationTo(Timestamp durationTo) { this.durationTo = durationTo; }
    public Long getAddedBy() { return addedBy; }
    public void setAddedBy(Long addedBy) { this.addedBy = addedBy; }
    public Long getDeletedBy() { return deletedBy; }
    public void setDeletedBy(Long deletedBy) { this.deletedBy = deletedBy; }
}
