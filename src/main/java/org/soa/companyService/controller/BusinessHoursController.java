package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.companyService.dto.CreateBusinessHoursRequest;
import org.soa.companyService.dto.UpdateBusinessHoursRequest;
import org.soa.companyService.model.BusinessHours;
import org.soa.companyService.model.Company;
import org.soa.companyService.service.BusinessHoursService;
import org.soa.companyService.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business-hours")
@Tag(name = "Business Hours", description = "Business Hours management API")
public class BusinessHoursController {

    @Autowired
    private BusinessHoursService businessHoursService;

    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Get all business hours", description = "Returns a list of all business hours")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BusinessHours.class)))
    })
    @GetMapping
    public List<BusinessHours> getAllBusinessHours() {
        return businessHoursService.getAllBusinessHours();
    }

    @Operation(summary = "Get business hours by ID", description = "Returns a single business hours entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the business hours",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BusinessHours.class))),
        @ApiResponse(responseCode = "404", description = "Business hours not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BusinessHours> getBusinessHoursById(
            @Parameter(description = "ID of the business hours to retrieve") @PathVariable Long id) {
        return businessHoursService.getBusinessHoursById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new business hours", description = "Creates a new business hours entry with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the business hours",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BusinessHours.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<BusinessHours> createBusinessHours(
            @Parameter(description = "Business hours creation request with all required details") @RequestBody CreateBusinessHoursRequest request) {
        BusinessHours businessHours = new BusinessHours();
        businessHours.setDayNumber(request.getDayNumber());
        businessHours.setTimeFrom(request.getTimeFrom());
        businessHours.setTimeTo(request.getTimeTo());
        businessHours.setPauseFrom(request.getPauseFrom());
        businessHours.setPauseTo(request.getPauseTo());
        businessHours.setDay(request.getDay());

        // Fetch and set Company
        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        businessHours.setCompany(company);

        BusinessHours createdBusinessHours = businessHoursService.createBusinessHours(businessHours);
        return ResponseEntity.ok(createdBusinessHours);
    }

    @Operation(summary = "Update business hours", description = "Updates an existing business hours entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the business hours",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BusinessHours.class))),
        @ApiResponse(responseCode = "404", description = "Business hours not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BusinessHours> updateBusinessHours(
            @Parameter(description = "ID of the business hours to update") @PathVariable Long id, 
            @Parameter(description = "Business hours update request with all required details") @RequestBody UpdateBusinessHoursRequest request) {
        try {
            BusinessHours businessHours = new BusinessHours();
            businessHours.setDayNumber(request.getDayNumber());
            businessHours.setTimeFrom(request.getTimeFrom());
            businessHours.setTimeTo(request.getTimeTo());
            businessHours.setPauseFrom(request.getPauseFrom());
            businessHours.setPauseTo(request.getPauseTo());
            businessHours.setDay(request.getDay());

            // Fetch and set Company
            Company company = companyService.getCompanyById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
            businessHours.setCompany(company);

            BusinessHours updatedBusinessHours = businessHoursService.updateBusinessHours(id, businessHours);
            return ResponseEntity.ok(updatedBusinessHours);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete business hours", description = "Deletes a business hours entry by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the business hours",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Business hours not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusinessHours(
            @Parameter(description = "ID of the business hours to delete") @PathVariable Long id) {
        businessHoursService.deleteBusinessHours(id);
        return ResponseEntity.noContent().build();
    }
}
