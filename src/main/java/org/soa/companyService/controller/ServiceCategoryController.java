package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.soa.companyService.dto.CreateServiceCategoryRequest;
import org.soa.companyService.dto.UpdateServiceCategoryRequest;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/service-categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Service Category", description = "Service Category management API")
@Validated
public class ServiceCategoryController {

    @Autowired private ServiceCategoryService serviceCategoryService;
    @Autowired private CompanyService companyService;

    @Operation(summary = "Get all service categories")
    @ApiResponse(responseCode = "200", description = "List of categories",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceCategory.class))))
    @GetMapping
    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryService.getAllServiceCategories();
    }

    @Operation(summary = "Get service category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = ServiceCategory.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategory> getServiceCategoryById(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
        return serviceCategoryService.getServiceCategoryById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Get service categories by company UUID")
    @ApiResponse(responseCode = "200", description = "Categories for company UUID",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceCategory.class))))
    @GetMapping("/company/{id}")
    public ResponseEntity<List<ServiceCategory>> getServiceCategoryByCompanyUUID(
            @Parameter(description = "Company UUID", example = "abc-uuid") @PathVariable String id) {
        return ResponseEntity.ok(serviceCategoryService.getServiceCategoriesByCompanyUUID(id));
    }

    @Operation(summary = "Create service category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = @Content(schema = @Schema(implementation = ServiceCategory.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateServiceCategoryRequest.class),
                    examples = @ExampleObject(value = "{\"name\":\"Hair\",\"companyId\":1}")))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceCategory> createServiceCategory(@Valid @RequestBody CreateServiceCategoryRequest request) {
        ServiceCategory cat = new ServiceCategory();
        cat.setName(request.getName());
        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        cat.setCompany(company);
        return ResponseEntity.ok(serviceCategoryService.createServiceCategory(cat));
    }

    @Operation(summary = "Update service category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(schema = @Schema(implementation = ServiceCategory.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = UpdateServiceCategoryRequest.class),
                    examples = @ExampleObject(value = "{\"name\":\"Massage\",\"companyId\":1}")))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceCategory> updateServiceCategory(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateServiceCategoryRequest request) {
        ServiceCategory cat = new ServiceCategory();
        cat.setName(request.getName());
        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        cat.setCompany(company);
        return ResponseEntity.ok(serviceCategoryService.updateServiceCategory(id, cat));
    }

    @Operation(summary = "Delete category")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceCategory(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
        serviceCategoryService.deleteServiceCategory(id);
        return ResponseEntity.noContent().build();
    }
}
