package org.soa.reservation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.reservation_service.exception.ErrorResponse;
import org.soa.reservation_service.client.EmployeeClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/_probe/employees", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Probe - Employees", description = "Lightweight passthroughs to Employee Service for debugging/integration checks")
public class EmployeeProbeController {

    private final EmployeeClient employeeClient;

    @Autowired
    public EmployeeProbeController(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    @Operation(summary = "Get raw employee payload")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee payload",
                    content = @Content(schema = @Schema(implementation = Object.class),
                            examples = @ExampleObject(value = "{\"id\":1,\"first_name\":\"Ana\",\"active\":true}"))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEmployee(
            @Parameter(description = "Employee ID", example = "1") @PathVariable Long id) {
        Map<String, Object> body = employeeClient.getEmployeeRaw(id);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get employee skills")
    @ApiResponse(responseCode = "200", description = "List of skills",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)),
                    examples = @ExampleObject(value = "[{\"service_id\":7},{\"service_id\":9}]")))
    @GetMapping("/{id}/skills")
    public ResponseEntity<List<Map<String, Object>>> getSkills(
            @Parameter(description = "Employee ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(employeeClient.getSkillsRaw(id));
    }

    @Operation(summary = "Get employee availability")
    @ApiResponse(responseCode = "200", description = "Availability rows",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)),
                    examples = @ExampleObject(value = "[{\"day_of_week\":1,\"time_from\":\"09:00:00\",\"time_to\":\"17:00:00\"}]")))
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<Map<String, Object>>> getAvailability(
            @Parameter(description = "Employee ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(employeeClient.getAvailabilityRaw(id));
    }

    @Operation(summary = "Assert employee is active", description = "Returns 204 if active; 404 otherwise.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee active – no content"),
            @ApiResponse(responseCode = "404", description = "Employee inactive or missing",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/assert-active")
    public ResponseEntity<Void> assertActive(
            @Parameter(description = "Employee ID", example = "1") @PathVariable Long id) {
        employeeClient.assertEmployeeActive(id); // throws if not found/inactive
        return ResponseEntity.noContent().build();
    }
}
