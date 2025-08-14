package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload to create a Service Category")
public class CreateServiceCategoryRequest {
    @NotBlank @Schema(example = "Hair") private String name;
    @NotNull @Schema(description = "FK to Company", example = "1") private Long companyId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
