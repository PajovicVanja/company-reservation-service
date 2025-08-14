package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to update a Location")
public class UpdateLocationRequest {
    @NotBlank @Schema(example = "Nova ulica") private String name;
    @NotBlank @Schema(example = "12") private String number;
    @Schema(example = "1") private Long parentLocationId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public Long getParentLocationId() { return parentLocationId; }
    public void setParentLocationId(Long parentLocationId) { this.parentLocationId = parentLocationId; }
}
