package org.soa.companyService.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_sms_notification", referencedColumnName = "id")
    private SmsNotificationConfig smsNotificationConfig;


    @Column(name = "id_picture")
    private String idPicture;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "id_auth_user")
    private String idAuthUser;

    @Column(name = "uuid_url")
    private String uuidUrl;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "company_name")
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "id_location_counrty", referencedColumnName = "id")
    private Location location;

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SmsNotificationConfig getSmsNotificationConfig() {
        return smsNotificationConfig;
    }

    public void setSmsNotificationConfig(SmsNotificationConfig smsNotificationConfig) {
        this.smsNotificationConfig = smsNotificationConfig;
    }

    public String getIdPicture() {
        return idPicture;
    }

    public void setIdPicture(String idPicture) {
        this.idPicture = idPicture;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
