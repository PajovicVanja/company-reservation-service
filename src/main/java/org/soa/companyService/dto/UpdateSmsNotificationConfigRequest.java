package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdateSmsNotificationConfigRequest {
    @NotBlank @Schema(example = "Default") private String name;
    @Schema(example = "Reminder: your appointment is tomorrow.") private String notificationMessage;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNotificationMessage() { return notificationMessage; }
    public void setNotificationMessage(String notificationMessage) { this.notificationMessage = notificationMessage; }
}
