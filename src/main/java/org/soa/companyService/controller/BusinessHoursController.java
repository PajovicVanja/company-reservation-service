package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.soa.companyService.dto.CreateBusinessHoursRequest;
import org.soa.companyService.dto.UpdateBusinessHoursRequest;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.model.BusinessHours;
import org.soa.companyService.model.Company;
import org.soa.companyService.service.BusinessHoursService;
import org.soa.companyService.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/business-hours", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Business Hours", description = "Business Hours management API")
@Validated
public class BusinessHoursController {

    @Autowired private BusinessHoursService businessHoursService;
    @Autowired private CompanyService companyService;

    @Operation(summary = "Get all business hours")
    @ApiResponse(responseCode = "200", description = "List of business hours",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BusinessHours.class))))
    @GetMapping
    public List<BusinessHours> getAllBusinessHours() {
        return businessHoursService.getAllBusinessHours();
    }

    @Operation(summary = "Get business hours by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry found",
                    content = @Content(schema = @Schema(implementation = BusinessHours.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BusinessHours> getBusinessHoursById(
            @Parameter(description = "Business hours ID", example = "1") @PathVariable Long id) {
        return businessHoursService.getBusinessHoursById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create new business hours")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = BusinessHours.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateBusinessHoursRequest.class),
                    examples = @ExampleObject(value = "{\"dayNumber\":1,\"timeFrom\":\"09:00:00\",\"timeTo\":\"17:00:00\"," +
                            "\"pauseFrom\":\"12:00:00\",\"pauseTo\":\"12:30:00\",\"companyId\":1,\"day\":\"MONDAY\"}")))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessHours> createBusinessHours(@Valid @RequestBody CreateBusinessHoursRequest request) {
        BusinessHours bh = new BusinessHours();
        bh.setDayNumber(request.getDayNumber());
        bh.setTimeFrom(request.getTimeFrom());
        bh.setTimeTo(request.getTimeTo());
        bh.setPauseFrom(request.getPauseFrom());
        bh.setPauseTo(request.getPauseTo());
        bh.setDay(request.getDay());

        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        bh.setCompany(company);

        BusinessHours created = businessHoursService.createBusinessHours(bh);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update business hours")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated",
                    content = @Content(schema = @Schema(implementation = BusinessHours.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = UpdateBusinessHoursRequest.class),
                    examples = @ExampleObject(value = "{\"dayNumber\":2,\"timeFrom\":\"10:00:00\",\"timeTo\":\"18:00:00\"," +
                            "\"companyId\":1,\"day\":\"TUESDAY\"}")))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessHours> updateBusinessHours(
            @Parameter(description = "Business hours ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateBusinessHoursRequest request) {
        BusinessHours bh = new BusinessHours();
        bh.setDayNumber(request.getDayNumber());
        bh.setTimeFrom(request.getTimeFrom());
        bh.setTimeTo(request.getTimeTo());
        bh.setPauseFrom(request.getPauseFrom());
        bh.setPauseTo(request.getPauseTo());
        bh.setDay(request.getDay());

        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        bh.setCompany(company);

        BusinessHours updated = businessHoursService.updateBusinessHours(id, bh);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete business hours")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessHours(
            @Parameter(description = "Business hours ID", example = "1") @PathVariable Long id) {
        businessHoursService.deleteBusinessHours(id);
        return ResponseEntity.noContent().build();
    }
}
