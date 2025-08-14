package org.soa.reservation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "Payload to create a reservation")
public class ReservationCreateRequest {

    @Schema(example = "2025-02-01T10:00:00.000+00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Timestamp date;

    @Schema(description = "FK to Company", example = "42", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long idCompany;

    @Schema(description = "FK to Service", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idService;

    @Schema(example = "123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer idCustomer;

    @Schema(example = "2025-02-01T09:00:00.000+00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Timestamp sendSms;

    @Schema(example = "123456", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long twoFACode;

    @Schema(example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private boolean hidden;

    @Schema(example = "john@example.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String customerEmail;

    @Schema(example = "38640111222", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long customerPhoneNumber;

    @Schema(example = "John Doe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String customerFullName;

    @Schema(description = "FK to Status", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long statusId;

    @Schema(description = "FK to Payment", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long paymentId;

    @Schema(description = "Plain employee id (no FK)", example = "321", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long employeeId;

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
    public Long getIdCompany() { return idCompany; }
    public void setIdCompany(Long idCompany) { this.idCompany = idCompany; }
    public Long getIdService() { return idService; }
    public void setIdService(Long idService) { this.idService = idService; }
    public Integer getIdCustomer() { return idCustomer; }
    public void setIdCustomer(Integer idCustomer) { this.idCustomer = idCustomer; }
    public Timestamp getSendSms() { return sendSms; }
    public void setSendSms(Timestamp sendSms) { this.sendSms = sendSms; }
    public Long getTwoFACode() { return twoFACode; }
    public void setTwoFACode(Long twoFACode) { this.twoFACode = twoFACode; }
    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Long getCustomerPhoneNumber() { return customerPhoneNumber; }
    public void setCustomerPhoneNumber(Long customerPhoneNumber) { this.customerPhoneNumber = customerPhoneNumber; }
    public String getCustomerFullName() { return customerFullName; }
    public void setCustomerFullName(String customerFullName) { this.customerFullName = customerFullName; }
    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}
