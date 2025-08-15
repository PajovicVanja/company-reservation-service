package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

@Schema(description = "Payload to send an SMS via sms-faas (GraphQL)")
public class SendSmsRequest {
    @NotBlank @Schema(example = "+38640111222")
    private String phone;

    @Schema(description = "Template ID to render (optional if 'message' is provided)", example = "appt-reminder")
    private String templateId;

    @Schema(description = "Raw message text (optional if 'templateId' is provided)", example = "Hello world!")
    private String message;

    @Schema(description = "Template variables (key/value). Only used when 'templateId' is provided.")
    private Map<String, String> variables;

    // getters/setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, String> getVariables() { return variables; }
    public void setVariables(Map<String, String> variables) { this.variables = variables; }
}
