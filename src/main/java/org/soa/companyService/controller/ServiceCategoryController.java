package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.companyService.dto.CreateServiceCategoryRequest;
import org.soa.companyService.dto.UpdateServiceCategoryRequest;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-categories")
@Tag(name = "Service Category", description = "Service Category management API")
public class ServiceCategoryController {

    @Autowired
    private ServiceCategoryService serviceCategoryService;

    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Get all service categories", description = "Returns a list of all service categories")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceCategory.class)))
    })
    @GetMapping
    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryService.getAllServiceCategories();
    }

    @Operation(summary = "Get service category by ID", description = "Returns a single service category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the service category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceCategory.class))),
        @ApiResponse(responseCode = "404", description = "Service category not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getServiceCategoryById(
            @Parameter(description = "ID of the service category to retrieve") @PathVariable Long id) {
        return serviceCategoryService.getServiceCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<List<ServiceCategory>> getServiceCategoryByCompanyUUID(
            @Parameter(description = "ID of the service category to retrieve") @PathVariable String id) {
        return ResponseEntity.ok(serviceCategoryService.getServiceCategoriesByCompanyUUID(id));
    }

    @Operation(summary = "Create a new service category", description = "Creates a new service category with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the service category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceCategory.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<ServiceCategory> createServiceCategory(
            @Parameter(description = "Service category creation request with all required details") @RequestBody CreateServiceCategoryRequest request) {
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setName(request.getName());

        // Fetch and set Company
        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        serviceCategory.setCompany(company);

        ServiceCategory createdServiceCategory = serviceCategoryService.createServiceCategory(serviceCategory);
        return ResponseEntity.ok(createdServiceCategory);
    }

    @Operation(summary = "Update a service category", description = "Updates an existing service category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the service category",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceCategory.class))),
        @ApiResponse(responseCode = "404", description = "Service category not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategory> updateServiceCategory(
            @Parameter(description = "ID of the service category to update") @PathVariable Long id, 
            @Parameter(description = "Service category update request with all required details") @RequestBody UpdateServiceCategoryRequest request) {
        try {
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setName(request.getName());

            // Fetch and set Company
            Company company = companyService.getCompanyById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
            serviceCategory.setCompany(company);

            ServiceCategory updatedServiceCategory = serviceCategoryService.updateServiceCategory(id, serviceCategory);
            return ResponseEntity.ok(updatedServiceCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a service category", description = "Deletes a service category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the service category",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Service category not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceCategory(
            @Parameter(description = "ID of the service category to delete") @PathVariable Long id) {
        serviceCategoryService.deleteServiceCategory(id);
        return ResponseEntity.noContent().build();
    }
}
