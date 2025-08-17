package org.soa.reservation_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    private Integer id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    // Optional; future-proof
    @JsonProperty("phone")
    private String phoneNumber;

    @JsonProperty("oauth_id")
    private String oauthId;

    @JsonProperty("location_id")
    private Integer locationId;

    // Fallback if some old endpoint returns "name"
    @JsonProperty("name")
    public void setNameFallback(String name) {
        if ((this.firstName == null || this.firstName.isBlank())
                && (this.lastName == null || this.lastName.isBlank())
                && name != null) {
            this.firstName = name;
            this.lastName = "";
        }
    }

    public String getFullName() {
        String fn = this.firstName != null ? this.firstName.trim() : "";
        String ln = this.lastName  != null ? this.lastName.trim()  : "";
        String full = (fn + " " + ln).trim();
        return full.isEmpty() ? null : full;
    }

    // getters/setters …
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getOauthId() { return oauthId; }
    public void setOauthId(String oauthId) { this.oauthId = oauthId; }
    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
}
