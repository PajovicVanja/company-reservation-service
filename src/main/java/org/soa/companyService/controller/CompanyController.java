package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.soa.companyService.dto.CreateCompanyRequest;
import org.soa.companyService.dto.UpdateCompanyRequest;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.Location;
import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.LocationService;
import org.soa.companyService.service.SmsNotificationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/companies", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Company", description = "Company management API")
@Validated
public class CompanyController {

    @Autowired private CompanyService companyService;
    @Autowired private SmsNotificationConfigService smsNotificationConfigService;
    @Autowired private LocationService locationService;

    @Operation(summary = "Get all companies", description = "Returns a list of all companies")
    @ApiResponse(responseCode = "200", description = "List of companies",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Company.class)),
                    examples = @ExampleObject(value = "[{\"id\":1,\"companyName\":\"Salon Nina\"}]")))
    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Operation(summary = "Get company by ID", description = "Returns a single company by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company found",
                    content = @Content(schema = @Schema(implementation = Company.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(
            @Parameter(description = "ID of the company to retrieve", example = "1") @PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create a new company", description = "Creates a new company with the provided information")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Company created",
                    content = @Content(schema = @Schema(implementation = Company.class),
                            examples = @ExampleObject(value = "{\"id\":10,\"companyName\":\"Barber Shop\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input or referenced entities not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateCompanyRequest.class),
                    examples = @ExampleObject(name = "CreateCompanyRequest",
                            value = "{\"description\":\"Men haircuts\",\"idAuthUser\":\"auth-123\",\"uuidUrl\":\"abc-uuid\"," +
                                    "\"address\":\"Main 1\",\"phoneNumber\":\"+38640111222\",\"email\":\"info@barber.si\"," +
                                    "\"firstName\":\"Nina\",\"lastName\":\"Kos\",\"companyName\":\"Barber Shop\"," +
                                    "\"locationId\":1,\"smsNotificationConfigId\":1}")))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        Company company = new Company();
        company.setDescription(request.getDescription());
        company.setIdAuthUser(request.getIdAuthUser());
        company.setUuidUrl(request.getUuidUrl());
        company.setAddress(request.getAddress());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setEmail(request.getEmail());
        company.setFirstName(request.getFirstName());
        company.setLastName(request.getLastName());
        company.setCompanyName(request.getCompanyName());

        Location location = locationService.getLocationById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id " + request.getLocationId()));
        company.setLocation(location);

        SmsNotificationConfig smsCfg = smsNotificationConfigService
                .getSmsNotificationConfigById(request.getSmsNotificationConfigId())
                .orElseThrow(() -> new RuntimeException("SmsNotificationConfig not found with id " + request.getSmsNotificationConfigId()));
        company.setSmsNotificationConfig(smsCfg);

        Company created = companyService.createCompany(company);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update a company", description = "Updates an existing company by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company updated",
                    content = @Content(schema = @Schema(implementation = Company.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or referenced entities not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = UpdateCompanyRequest.class),
                    examples = @ExampleObject(value = "{\"description\":\"Updated\",\"address\":\"New 2\"," +
                            "\"phoneNumber\":\"+38640123456\",\"email\":\"contact@barber.si\",\"firstName\":\"Nina\"," +
                            "\"lastName\":\"Kos\",\"companyName\":\"Barber Shop\",\"locationId\":1,\"smsNotificationConfigId\":1}")))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> updateCompany(
            @Parameter(description = "ID of the company to update", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyRequest request) {
        Company company = new Company();
        company.setDescription(request.getDescription());
        company.setIdPicture(request.getIdPicture());
        company.setAddress(request.getAddress());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setEmail(request.getEmail());
        company.setFirstName(request.getFirstName());
        company.setLastName(request.getLastName());
        company.setCompanyName(request.getCompanyName());

        Location location = locationService.getLocationById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found with id " + request.getLocationId()));
        company.setLocation(location);

        SmsNotificationConfig smsCfg = smsNotificationConfigService
                .getSmsNotificationConfigById(request.getSmsNotificationConfigId())
                .orElseThrow(() -> new RuntimeException("SmsNotificationConfig not found with id " + request.getSmsNotificationConfigId()));
        company.setSmsNotificationConfig(smsCfg);

        Company updated = companyService.updateCompany(id, company);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a company", description = "Deletes a company by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Company deleted"),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @Parameter(description = "ID of the company to delete", example = "1") @PathVariable Long id) {
        companyService.deleteCompany(id); // 404 mapped by advice if missing
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload company picture", description = "Uploads a picture for a specific company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Picture uploaded"),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Upload failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/{id}/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPicture(
            @Parameter(description = "ID of the company to upload picture for", example = "1") @PathVariable Long id,
            @Parameter(description = "Picture file to upload") @RequestPart("file") MultipartFile file) {
        companyService.uploadPicture(id, file);
        return ResponseEntity.ok().build();
    }
}
