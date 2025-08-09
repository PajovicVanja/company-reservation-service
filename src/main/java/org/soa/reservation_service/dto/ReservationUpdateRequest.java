package org.soa.reservation_service.dto;

import java.sql.Timestamp;

public class ReservationUpdateRequest {

    private Timestamp date;
    private Long idCompany;
    private Long idService;
    private Integer idCustomer;
    private Timestamp sendSms;
    private Long twoFACode;
    private boolean hidden;
    private String customerEmail;
    private Long customerPhoneNumber;
    private String customerFullName;
    private Long statusId;
    private Long paymentId;

    // NEW: carry employee id (no relation/FK)
    private Long employeeId;

    // Getters and Setters

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Long getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }

    public Long getIdService() {
        return idService;
    }

    public void setIdService(Long idService) {
        this.idService = idService;
    }

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
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

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
