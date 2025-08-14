package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Payload to create a Company")
public class CreateCompanyRequest {

    @Schema(example = "Men haircuts") private String description;

    @Schema(description = "Public image object key or path", example = "profilePicture_1.png")
    private String idPicture;

    @Schema(example = "auth-123") private String idAuthUser;
    @Schema(example = "abc-uuid") private String uuidUrl;

    @NotBlank @Schema(example = "Main street 1") private String address;
    @NotBlank @Schema(example = "+38640111222") private String phoneNumber;
    @Email @NotBlank @Schema(example = "info@barber.si") private String email;
    @Schema(example = "Nina") private String firstName;
    @Schema(example = "Kos") private String lastName;
    @NotBlank @Schema(example = "Barber Shop") private String companyName;

    @NotNull @Schema(description = "FK to Location", example = "1") private Long locationId;
    @NotNull @Schema(description = "FK to SmsNotificationConfig", example = "1") private Long smsNotificationConfigId;

    // getters/setters...
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIdPicture() { return idPicture; }
    public void setIdPicture(String idPicture) { this.idPicture = idPicture; }
    public String getIdAuthUser() { return idAuthUser; }
    public void setIdAuthUser(String idAuthUser) { this.idAuthUser = idAuthUser; }
    public String getUuidUrl() { return uuidUrl; }
    public void setUuidUrl(String uuidUrl) { this.uuidUrl = uuidUrl; }
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
