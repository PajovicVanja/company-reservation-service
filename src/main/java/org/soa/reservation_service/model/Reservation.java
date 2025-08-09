package org.soa.reservation_service.model;

import jakarta.persistence.*;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceM;

import java.sql.Timestamp;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "date_to")
    private Timestamp dateTo;

    @ManyToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "id_service", referencedColumnName = "id")
    private ServiceM service;

    @Column(name = "id_customer")
    private Integer idCustomer;

    // Added as a simple scalar field (no relation/FK)
    @Column(name = "id_employee")
    private Long employeeId;

    @Column(name = "send_sms")
    private Timestamp sendSms;

    @Column(name = "2FA_code")
    private Long twoFACode;

    @Column(name = "hidden")
    private boolean hidden;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone_number")
    private Long customerPhoneNumber;

    @Column(name = "customer_full_name")
    private String customerFullName;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "notified")
    private boolean notified;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getDateTo() {
        return dateTo;
    }

    public void setDateTo(Timestamp dateTo) {
        this.dateTo = dateTo;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ServiceM getService() {
        return service;
    }

    public void setService(ServiceM service) {
        this.service = service;
    }

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Timestamp getSendSms() {
        return sendSms;
    }

    public void setSendSms(Timestamp sendSms) {
        this.sendSms = sendSms;
    }

    public Long getTwoFACode() {
        return twoFACode;
    }

    public void setTwoFACode(Long twoFACode) {
        this.twoFACode = twoFACode;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(Long customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public boolean isNotified() {
        return notified;
    }

    // Kept for compatibility with existing code that may call getNotified()
    public boolean getNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
