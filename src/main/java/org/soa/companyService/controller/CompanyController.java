package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.companyService.dto.CreateCompanyRequest;
import org.soa.companyService.dto.UpdateCompanyRequest;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.Location;
import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.LocationService;
import org.soa.companyService.service.SmsNotificationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company", description = "Company management API")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SmsNotificationConfigService smsNotificationConfigService;

    @Autowired
    private LocationService locationService;

    @Operation(summary = "Get all companies", description = "Returns a list of all companies")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Company.class)))
    })
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Operation(summary = "Get company by ID", description = "Returns a single company by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the company",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Company.class))),
        @ApiResponse(responseCode = "404", description = "Company not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(
            @Parameter(description = "ID of the company to retrieve") @PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new company", description = "Creates a new company with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the company",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Company.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Company> createCompany(
            @Parameter(description = "Company creation request with all required details") @RequestBody CreateCompanyRequest request) {
        // Map DTO to entity
        Company company = new Company();
        company.setDescription(request.getDescription());
        company.setIdAuthUser(request.getIdAuthUser());
        company.setUuidUrl(request.getUuidUrl()); // generisati uuidUrl
        company.setAddress(request.getAddress());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setEmail(request.getEmail());
        company.setFirstName(request.getFirstName());
        company.setLastName(request.getLastName());
        company.setCompanyName(request.getCompanyName());

        // Fetch and set Location
        Location location = locationService.getLocationById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id " + request.getLocationId()));
        company.setLocation(location);

        // Fetch and set SmsNotificationConfig
        SmsNotificationConfig smsNotificationConfig = smsNotificationConfigService
                .getSmsNotificationConfigById(request.getSmsNotificationConfigId())
                .orElseThrow(() -> new RuntimeException("SmsNotificationConfig not found with id " + request.getSmsNotificationConfigId()));
        company.setSmsNotificationConfig(smsNotificationConfig);

        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.ok(createdCompany);
    }

    @Operation(summary = "Update a company", description = "Updates an existing company by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the company",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Company.class))),
        @ApiResponse(responseCode = "404", description = "Company not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
            @Parameter(description = "ID of the company to update") @PathVariable Long id, 
            @Parameter(description = "Company update request with all required details") @RequestBody UpdateCompanyRequest request) {
        try {
            // Map DTO to entity
            Company company = new Company();
            company.setDescription(request.getDescription());
            company.setIdPicture(request.getIdPicture());
            company.setAddress(request.getAddress());
            company.setPhoneNumber(request.getPhoneNumber());
            company.setEmail(request.getEmail());
            company.setFirstName(request.getFirstName());
            company.setLastName(request.getLastName());
            company.setCompanyName(request.getCompanyName());

            // Fetch and set Location
            Location location = locationService.getLocationById(request.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found with id " + request.getLocationId()));
            company.setLocation(location);

            // Fetch and set SmsNotificationConfig
            SmsNotificationConfig smsNotificationConfig = smsNotificationConfigService
                    .getSmsNotificationConfigById(request.getSmsNotificationConfigId())
                    .orElseThrow(() -> new RuntimeException("SmsNotificationConfig not found with id " + request.getSmsNotificationConfigId()));
            company.setSmsNotificationConfig(smsNotificationConfig);

            Company updatedCompany = companyService.updateCompany(id, company);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a company", description = "Deletes a company by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the company",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Company not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @Parameter(description = "ID of the company to delete") @PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload company picture", description = "Uploads a picture for a specific company")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully uploaded the picture",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Company not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error during upload",
                content = @Content)
    })
    @PostMapping("/{id}/upload-picture")
    public ResponseEntity<Void> uploadPicture(
            @Parameter(description = "ID of the company to upload picture for") @PathVariable Long id, 
            @Parameter(description = "Picture file to upload", required = true) @RequestParam("file") MultipartFile file) {
        try {
            companyService.uploadPicture(id, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
