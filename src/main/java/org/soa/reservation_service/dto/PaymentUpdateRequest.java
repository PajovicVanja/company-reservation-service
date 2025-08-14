package org.soa.reservation_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Schema(description = "Payload to update a payment")
public class PaymentUpdateRequest {

    @Schema(description = "FK to Payment Type", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long paymentTypeId;

    @Schema(example = "REFUNDED", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;

    @Schema(example = "2025-02-01T12:00:00.000+00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Timestamp datePaid;

    @Schema(example = "49.99", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal amount;

    public Long getPaymentTypeId() { return paymentTypeId; }
    public void setPaymentTypeId(Long paymentTypeId) { this.paymentTypeId = paymentTypeId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getDatePaid() { return datePaid; }
    public void setDatePaid(Timestamp datePaid) { this.datePaid = datePaid; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
