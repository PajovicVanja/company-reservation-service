package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.companyService.dto.CreateServiceRequest;
import org.soa.companyService.dto.UpdateServiceRequest;
import org.soa.companyService.model.Company;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.model.ServiceM;
import org.soa.companyService.service.CompanyService;
import org.soa.companyService.service.ServiceCategoryService;
import org.soa.companyService.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@Tag(name = "Service", description = "Service management API")
public class ServiceController {

    Logger logger = LoggerFactory.getLogger(ServiceController.class);
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceCategoryService serviceCategoryService;

    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Get all services", description = "Returns a list of all services")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceM.class)))
    })
    @GetMapping("company/{idCompany}")
    public ResponseEntity<List<ServiceM>> getAllServices(@PathVariable Long idCompany) {
        return ResponseEntity.ok(serviceService.getAllServices(idCompany));
    }

    @GetMapping("company/{idCompany}/category/{idCategory}")
    public ResponseEntity<List<ServiceM>> getAllServicesByCategory(
            @PathVariable Long idCompany,
            @PathVariable Long idCategory
    ) {
        return ResponseEntity.ok(serviceService.getAllServices(idCompany, idCategory));
    }

    @Operation(summary = "Get service by ID", description = "Returns a single service by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the service",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceM.class))),
        @ApiResponse(responseCode = "404", description = "Service not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceM> getServiceById(
            @Parameter(description = "ID of the service to retrieve") @PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new service", description = "Creates a new service with the provided information and optional picture")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the service",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceM.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error during file upload",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<ServiceM> createService(
            @Parameter(description = "Service creation request with all required details") @RequestBody CreateServiceRequest request,  
            @Parameter(description = "Picture file to upload for the service") @RequestParam("file") MultipartFile file) {
        ServiceM service = new ServiceM();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setIdPicture(request.getIdPicture());
        service.setDuration(request.getDuration());

        // Fetch and set category
        ServiceCategory category = serviceCategoryService.getServiceCategoryById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id " + request.getCategoryId()));
        service.setCategory(category);

        // Fetch and set company
        Company company = companyService.getCompanyById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
        service.setCompany(company);
        if (file != null && !file.isEmpty()) {
            try {
                ServiceM createdService = serviceService.createService(service, file);
                return ResponseEntity.ok(createdService);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            ServiceM createdService = serviceService.createService(service);
            return ResponseEntity.ok(createdService);
        }
    }

    @Operation(summary = "Update a service", description = "Updates an existing service by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the service",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ServiceM.class))),
        @ApiResponse(responseCode = "404", description = "Service not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceM> updateService(
            @Parameter(description = "ID of the service to update") @PathVariable Long id, 
            @Parameter(description = "Service update request with all required details") @RequestBody UpdateServiceRequest request) {
        try {
            ServiceM service = new ServiceM();
            service.setName(request.getName());
            service.setDescription(request.getDescription());
            service.setPrice(request.getPrice());
            service.setIdPicture(request.getIdPicture());
            service.setDuration(request.getDuration());

            // Fetch and set category
            ServiceCategory category = serviceCategoryService.getServiceCategoryById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id " + request.getCategoryId()));
            service.setCategory(category);

            // Fetch and set company
            Company company = companyService.getCompanyById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found with id " + request.getCompanyId()));
            service.setCompany(company);

            ServiceM updatedService = serviceService.updateService(id, service);
            return ResponseEntity.ok(updatedService);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a service", description = "Deletes a service by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the service",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Service not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @Parameter(description = "ID of the service to delete") @PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Upload service picture", description = "Uploads a picture for a specific service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully uploaded the picture",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Service not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error during upload",
                content = @Content)
    })
    @PostMapping("/{id}/upload-picture")
    public ResponseEntity<Void> uploadPicture(
            @Parameter(description = "ID of the service to upload picture for") @PathVariable Long id, 
            @Parameter(description = "Picture file to upload", required = true) @RequestParam("file") MultipartFile file) {
        logger.info("Uploading picture for service with ID: {}", id);
        try {
            serviceService.uploadPicture(id, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error uploading picture for service with ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
