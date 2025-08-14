package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.companyService.dto.CreateServiceRequest;
import org.soa.companyService.dto.UpdateServiceRequest;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.model.ServiceM;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceCategoryService;
import org.soa.companyService.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Service", description = "Service management API")
@Validated
public class ServiceController {

    Logger logger = LoggerFactory.getLogger(ServiceController.class);
    @Autowired private ServiceService serviceService;
    @Autowired private ServiceCategoryService serviceCategoryService;
    @Autowired private CompanyService companyService;

    @Operation(summary = "List services for a company")
    @ApiResponse(responseCode = "200", description = "List of services",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceM.class))))
    @GetMapping("company/{idCompany}")
    public ResponseEntity<List<ServiceM>> getAllServices(
            @Parameter(description = "Company ID", example = "1") @PathVariable Long idCompany) {
        return ResponseEntity.ok(serviceService.getAllServices(idCompany));
    }

    @Operation(summary = "List services for a company and category")
    @ApiResponse(responseCode = "200", description = "List filtered by category",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceM.class))))
    @GetMapping("company/{idCompany}/category/{idCategory}")
    public ResponseEntity<List<ServiceM>> getAllServicesByCategory(
            @Parameter(example = "1") @PathVariable Long idCompany,
            @Parameter(example = "2") @PathVariable Long idCategory) {
        return ResponseEntity.ok(serviceService.getAllServices(idCompany, idCategory));
    }

    @Operation(summary = "Get service by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service found",
                    content = @Content(schema = @Schema(implementation = ServiceM.class))),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceM> getServiceById(
            @Parameter(description = "Service ID", example = "10") @PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create a new service (JSON only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service created",
                    content = @Content(schema = @Schema(implementation = ServiceM.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or referenced entities missing",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateServiceRequest.class),
                    examples = @ExampleObject(value = "{\"categoryId\":2,\"companyId\":1,\"name\":\"Massage\"," +
                            "\"description\":\"Relax\",\"price\":50.0,\"duration\":60}")))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceM> createService(@Valid @RequestBody CreateServiceRequest request) {
        ServiceM service = new ServiceM();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setIdPicture(request.getIdPicture());
        service.setDuration(request.getDuration());

        ServiceCategory category = serviceCategoryService.getServiceCategoryById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id " + request.getCategoryId()));
        service.setCategory(category);

        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        service.setCompany(company);

        ServiceM created = serviceService.createService(service);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Create a new service with picture (multipart/form-data)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Service created with image",
                    content = @Content(schema = @Schema(implementation = ServiceM.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or upload error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/with-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceM> createServiceWithPicture(
            @Parameter(description = "Service JSON payload", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateServiceRequest.class)))
            @RequestPart("request") @Valid CreateServiceRequest request,
            @Parameter(description = "Picture file to upload") @RequestPart(value = "file", required = false) MultipartFile file) {

        ServiceM service = new ServiceM();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setIdPicture(request.getIdPicture());
        service.setDuration(request.getDuration());

        ServiceCategory category = serviceCategoryService.getServiceCategoryById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id " + request.getCategoryId()));
        service.setCategory(category);

        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        service.setCompany(company);

        ServiceM created = (file != null && !file.isEmpty())
                ? serviceService.createService(service, file)
                : serviceService.createService(service);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update a service")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service updated",
                    content = @Content(schema = @Schema(implementation = ServiceM.class))),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = UpdateServiceRequest.class),
                    examples = @ExampleObject(value = "{\"categoryId\":2,\"companyId\":1,\"name\":\"Massage Deluxe\"," +
                            "\"description\":\"Relax++\",\"price\":65.0,\"duration\":75}")))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceM> updateService(
            @Parameter(description = "Service ID", example = "10") @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRequest request) {
        ServiceM s = new ServiceM();
        s.setName(request.getName());
        s.setDescription(request.getDescription());
        s.setPrice(request.getPrice());
        s.setIdPicture(request.getIdPicture());
        s.setDuration(request.getDuration());

        ServiceCategory category = serviceCategoryService.getServiceCategoryById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id " + request.getCategoryId()));
        s.setCategory(category);

        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        s.setCompany(company);

        return ResponseEntity.ok(serviceService.updateService(id, s));
    }

    @Operation(summary = "Delete a service")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Service deleted"),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @Parameter(description = "Service ID", example = "10") @PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload service picture")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Picture uploaded"),
            @ApiResponse(responseCode = "404", description = "Service not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Upload failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/{id}/upload-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadPicture(
            @Parameter(description = "Service ID", example = "10") @PathVariable Long id,
            @Parameter(description = "Picture file") @RequestPart("file") MultipartFile file) {
        logger.info("Uploading picture for service {}", id);
        serviceService.uploadPicture(id, file);
        return ResponseEntity.ok().build();
    }
}
