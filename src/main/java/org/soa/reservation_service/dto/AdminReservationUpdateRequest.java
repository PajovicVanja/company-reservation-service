package org.soa.reservation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Admin update payload (hide and set status)")
public class AdminReservationUpdateRequest {
    @Schema(example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long statusId;

    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}
