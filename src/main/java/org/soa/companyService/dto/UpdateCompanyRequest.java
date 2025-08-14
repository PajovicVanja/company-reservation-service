package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload to update a Company")
public class UpdateCompanyRequest {

    @Schema(example = "Updated desc") private String description;
    @Schema(example = "profilePicture_1.png") private String idPicture;

    @Schema(example = "Main 2") private String address;
    @Schema(example = "+38640123456") private String phoneNumber;
    @Email @Schema(example = "contact@barber.si") private String email;
    @Schema(example = "Nina") private String firstName;
    @Schema(example = "Kos") private String lastName;
    @Schema(example = "Barber Shop") private String companyName;

    @NotNull @Schema(example = "1") private Long locationId;
    @NotNull @Schema(example = "1") private Long smsNotificationConfigId;

    // getters/setters...
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIdPicture() { return idPicture; }
    public void setIdPicture(String idPicture) { this.idPicture = idPicture; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public Long getSmsNotificationConfigId() { return smsNotificationConfigId; }
    public void setSmsNotificationConfigId(Long smsNotificationConfigId) { this.smsNotificationConfigId = smsNotificationConfigId; }
}
