package org.soa.companyService.dto;

public class CreateCompanyRequest {

    private String description;
    private String idPicture;
    private String idAuthUser;
    private String uuidUrl;
    private String address;
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private Long locationId;
    private Long smsNotificationConfigId;

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(String idPicture) {
        this.idPicture = idPicture;
    }

    public String getIdAuthUser() {
        return idAuthUser;
    }

    public void setIdAuthUser(String idAuthUser) {
        this.idAuthUser = idAuthUser;
    }

    public String getUuidUrl() {
        return uuidUrl;
    }

    public void setUuidUrl(String uuidUrl) {
        this.uuidUrl = uuidUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getSmsNotificationConfigId() {
        return smsNotificationConfigId;
    }

    public void setSmsNotificationConfigId(Long smsNotificationConfigId) {
        this.smsNotificationConfigId = smsNotificationConfigId;
    }
}
