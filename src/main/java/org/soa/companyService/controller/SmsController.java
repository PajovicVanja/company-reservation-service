package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.soa.companyService.dto.SendSmsRequest;
import org.soa.companyService.dto.SendSmsResponse;
import org.soa.companyService.exception.BadRequestException;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.service.SmsFaasClient;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/notifications/sms", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications - SMS", description = "Proxy endpoints that send SMS via the sms-faas GraphQL on Vercel")
@Validated
public class SmsController {

    private final SmsFaasClient smsFaasClient;

    public SmsController(SmsFaasClient smsFaasClient) {
        this.smsFaasClient = smsFaasClient;
    }

    @Operation(summary = "Send an SMS (via sms-faas GraphQL)",
            description = "If 'templateId' is provided, 'variables' will be used to render the message. Otherwise, provide a 'message'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS send attempt result",
                    content = @Content(schema = @Schema(implementation = SendSmsResponse.class),
                            examples = @ExampleObject(value = "{\"ok\":true,\"status\":\"SENT\",\"id\":\"689f7b...\",\"phone\":\"+38640111222\",\"message\":\"Hi Ana, your appointment is at 10:30.\",\"createdAt\":\"2025-08-15T18:23:02.376Z\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2025-08-14T08:30:45.123+02:00\",\"status\":400,\"error\":\"Bad Request\",\"message\":\"Either templateId or message must be provided\",\"path\":\"/api/notifications/sms/send\"}"))),
            @ApiResponse(responseCode = "502", description = "sms-faas upstream error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"timestamp\":\"2025-08-14T08:30:45.123+02:00\",\"status\":502,\"error\":\"Bad Gateway\",\"message\":\"sms-faas HTTP 500: ...\",\"path\":\"/api/notifications/sms/send\"}")))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = SendSmsRequest.class),
                    examples = {
                            @ExampleObject(name = "Using template", value =
                                    "{\"phone\":\"+38640111222\",\"templateId\":\"appt-reminder\",\"variables\":{\"name\":\"Ana\",\"time\":\"10:30\"}}"),
                            @ExampleObject(name = "Using raw message", value =
                                    "{\"phone\":\"+38640111222\",\"message\":\"Hello from Company API!\"}")
                    }))
    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendSms(@Valid @RequestBody SendSmsRequest req) {
        if ((req.getTemplateId() == null || req.getTemplateId().isBlank())
                && (req.getMessage() == null || req.getMessage().isBlank())) {
            throw new BadRequestException("Either templateId or message must be provided");
        }

        try {
            Map<String, Object> result = smsFaasClient.sendSms(
                    req.getPhone(),
                    req.getTemplateId(),
                    req.getMessage(),
                    req.getVariables()
            );

            SendSmsResponse out = new SendSmsResponse(
                    "SENT".equalsIgnoreCase((String) result.get("status")),
                    (String) result.get("status"),
                    (String) result.get("id"),
                    (String) result.get("phone"),
                    (String) result.get("message"),
                    (String) result.get("createdAt")
            );
            return ResponseEntity.ok(out);

        } catch (Exception e) {
            // Preserve 502 semantics with a proper error body
            ErrorResponse err = new ErrorResponse(
                    OffsetDateTime.now(), 502, "Bad Gateway",
                    e.getMessage(), "/api/notifications/sms/send"
            );
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(err);
        }
    }
}
