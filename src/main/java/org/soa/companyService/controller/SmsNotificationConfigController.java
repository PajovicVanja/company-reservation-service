package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.soa.companyService.dto.CreateSmsNotificationConfigRequest;
import org.soa.companyService.dto.UpdateSmsNotificationConfigRequest;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.SmsNotificationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/sms-notification-configs", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "SMS Notification Config", description = "SMS Notification Configuration API")
@Validated
public class SmsNotificationConfigController {

    @Autowired private SmsNotificationConfigService smsNotificationConfigService;

    @Operation(summary = "Get all SMS notification configurations")
    @ApiResponse(responseCode = "200", description = "List of configs",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = SmsNotificationConfig.class))))
    @GetMapping
    public List<SmsNotificationConfig> getAllSmsNotificationConfigs() {
        return smsNotificationConfigService.getAllSmsNotificationConfigs();
    }

    @Operation(summary = "Get SMS notification configuration by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Config found",
                    content = @Content(schema = @Schema(implementation = SmsNotificationConfig.class))),
            @ApiResponse(responseCode = "404", description = "Config not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SmsNotificationConfig> getSmsNotificationConfigById(
            @Parameter(description = "Config ID", example = "1") @PathVariable Long id) {
        return smsNotificationConfigService.getSmsNotificationConfigById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create a new SMS notification configuration")
    @ApiResponse(responseCode = "201", description = "Config created",
            content = @Content(schema = @Schema(implementation = SmsNotificationConfig.class)))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateSmsNotificationConfigRequest.class),
                    examples = @ExampleObject(value = "{\"name\":\"Default\",\"notificationMessage\":\"Reminder...\"}")))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsNotificationConfig> createSmsNotificationConfig(
            @Valid @RequestBody CreateSmsNotificationConfigRequest request) {
        SmsNotificationConfig cfg = new SmsNotificationConfig();
        cfg.setName(request.getName());
        cfg.setNotificationMessage(request.getNotificationMessage());
        SmsNotificationConfig created = smsNotificationConfigService.createSmsNotificationConfig(cfg);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an SMS notification configuration")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Config updated",
                    content = @Content(schema = @Schema(implementation = SmsNotificationConfig.class))),
            @ApiResponse(responseCode = "404", description = "Config not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = UpdateSmsNotificationConfigRequest.class),
                    examples = @ExampleObject(value = "{\"name\":\"Default\",\"notificationMessage\":\"Updated text\"}")))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsNotificationConfig> updateSmsNotificationConfig(
            @Parameter(description = "Config ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateSmsNotificationConfigRequest request) {
        SmsNotificationConfig cfg = new SmsNotificationConfig();
        cfg.setName(request.getName());
        cfg.setNotificationMessage(request.getNotificationMessage());
        return ResponseEntity.ok(smsNotificationConfigService.updateSmsNotificationConfig(id, cfg));
    }

    @Operation(summary = "Delete an SMS notification configuration")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Config deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSmsNotificationConfig(
            @Parameter(description = "Config ID", example = "1") @PathVariable Long id) {
        smsNotificationConfigService.deleteSmsNotificationConfig(id);
        return ResponseEntity.noContent().build();
    }
}
