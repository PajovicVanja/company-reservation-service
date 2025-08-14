package org.soa.reservation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to update a status")
public class StatusUpdateRequest {

    @Schema(example = "Confirmed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    @Schema(example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long addedBy;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getAddedBy() { return addedBy; }
    public void setAddedBy(Long addedBy) { this.addedBy = addedBy; }
}
