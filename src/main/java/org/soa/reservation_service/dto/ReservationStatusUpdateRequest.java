package org.soa.reservation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to update reservation status")
public class ReservationStatusUpdateRequest {
    @Schema(example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long statusId;

    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
}
