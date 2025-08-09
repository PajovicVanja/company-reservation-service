package org.soa.reservation_service.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PaymentUpdateRequest {

    private Long paymentTypeId;
    private String status;
    private Timestamp datePaid;
    private BigDecimal amount;

    // Getters and Setters

    public Long getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Timestamp datePaid) {
        this.datePaid = datePaid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
