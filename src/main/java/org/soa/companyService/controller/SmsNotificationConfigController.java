package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.companyService.dto.CreateSmsNotificationConfigRequest;
import org.soa.companyService.dto.UpdateSmsNotificationConfigRequest;
import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.SmsNotificationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sms-notification-configs")
@Tag(name = "SMS Notification Config", description = "SMS Notification Configuration API")
public class SmsNotificationConfigController {

    @Autowired
    private SmsNotificationConfigService smsNotificationConfigService;

    @Operation(summary = "Get all SMS notification configurations", description = "Returns a list of all SMS notification configurations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SmsNotificationConfig.class)))
    })
    @GetMapping
    public List<SmsNotificationConfig> getAllSmsNotificationConfigs() {
        return smsNotificationConfigService.getAllSmsNotificationConfigs();
    }

    @Operation(summary = "Get SMS notification configuration by ID", description = "Returns a single SMS notification configuration by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the configuration",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SmsNotificationConfig.class))),
        @ApiResponse(responseCode = "404", description = "Configuration not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SmsNotificationConfig> getSmsNotificationConfigById(
            @Parameter(description = "ID of the SMS notification configuration to retrieve") @PathVariable Long id) {
        return smsNotificationConfigService.getSmsNotificationConfigById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new SMS notification configuration", description = "Creates a new SMS notification configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the configuration",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SmsNotificationConfig.class)))
    })
    @PostMapping
    public ResponseEntity<SmsNotificationConfig> createSmsNotificationConfig(
            @Parameter(description = "SMS notification configuration details") @RequestBody CreateSmsNotificationConfigRequest request) {
        SmsNotificationConfig smsNotificationConfig = new SmsNotificationConfig();
        smsNotificationConfig.setName(request.getName());

        SmsNotificationConfig createdSmsNotificationConfig = smsNotificationConfigService.createSmsNotificationConfig(smsNotificationConfig);
        return ResponseEntity.ok(createdSmsNotificationConfig);
    }

    @Operation(summary = "Update an SMS notification configuration", description = "Updates an existing SMS notification configuration by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the configuration",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SmsNotificationConfig.class))),
        @ApiResponse(responseCode = "404", description = "Configuration not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SmsNotificationConfig> updateSmsNotificationConfig(
            @Parameter(description = "ID of the SMS notification configuration to update") @PathVariable Long id,
            @Parameter(description = "Updated SMS notification configuration details") @RequestBody UpdateSmsNotificationConfigRequest request) {
        try {
            SmsNotificationConfig smsNotificationConfig = new SmsNotificationConfig();
            smsNotificationConfig.setName(request.getName());

            SmsNotificationConfig updatedSmsNotificationConfig = smsNotificationConfigService.updateSmsNotificationConfig(id, smsNotificationConfig);
            return ResponseEntity.ok(updatedSmsNotificationConfig);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete an SMS notification configuration", description = "Deletes an SMS notification configuration by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the configuration",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSmsNotificationConfig(
            @Parameter(description = "ID of the SMS notification configuration to delete") @PathVariable Long id) {
        smsNotificationConfigService.deleteSmsNotificationConfig(id);
        return ResponseEntity.noContent().build();
    }
}
