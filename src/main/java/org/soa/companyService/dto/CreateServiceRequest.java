package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Payload to create a Service")
public class CreateServiceRequest {
    @NotNull @Schema(example = "2") private Long categoryId;
    @NotNull @Schema(example = "1") private Long companyId;
    @NotBlank @Schema(example = "Massage") private String name;
    @Schema(example = "Relax") private String description;
    @NotNull @Schema(example = "50.0") private Float price;
    @Schema(example = "service_picture.png") private String idPicture;
    @NotNull @Schema(example = "60") private Short duration;

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
    public String getIdPicture() { return idPicture; }
    public void setIdPicture(String idPicture) { this.idPicture = idPicture; }
    public Short getDuration() { return duration; }
    public void setDuration(Short duration) { this.duration = duration; }
}
