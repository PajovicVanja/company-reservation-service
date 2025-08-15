package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of sending an SMS via sms-faas")
public class SendSmsResponse {
    @Schema(example = "true") private boolean ok;
    @Schema(example = "SENT") private String status;
    @Schema(example = "689f7b068dca95494412ae19") private String id;
    @Schema(example = "+38640111222") private String phone;
    @Schema(example = "Hi Ana, your appointment is at 10:30.") private String message;
    @Schema(example = "2025-08-15T18:23:02.376Z") private String createdAt;

    public SendSmsResponse() {}

    public SendSmsResponse(boolean ok, String status, String id, String phone, String message, String createdAt) {
        this.ok = ok; this.status = status; this.id = id; this.phone = phone; this.message = message; this.createdAt = createdAt;
    }

    public boolean isOk() { return ok; }
    public void setOk(boolean ok) { this.ok = ok; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
