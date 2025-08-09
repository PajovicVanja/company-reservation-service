package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.companyService.dto.CreateLocationRequest;
import org.soa.companyService.dto.UpdateLocationRequest;
import org.soa.companyService.model.Location;
import org.soa.companyService.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "Location", description = "Location management API")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Operation(summary = "Get all locations", description = "Returns a list of all locations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Location.class)))
    })
    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @Operation(summary = "Get location by ID", description = "Returns a single location by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the location",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Location.class))),
        @ApiResponse(responseCode = "404", description = "Location not found",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(
            @Parameter(description = "ID of the location to retrieve") @PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new location", description = "Creates a new location with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the location",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Location.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Location> createLocation(
            @Parameter(description = "Location creation request with all required details") @RequestBody CreateLocationRequest request) {
        Location location = new Location();
        location.setStreet(request.getName());
        location.setNumber(request.getNumber());

        // Fetch and set Parent Location if provided
        if (request.getParentLocationId() != null) {
            Location parentLocation = locationService.getLocationById(request.getParentLocationId())
                    .orElseThrow(() -> new RuntimeException("Parent Location not found with id " + request.getParentLocationId()));
            location.setParentLocation(parentLocation);
        }

        Location createdLocation = locationService.createLocation(location);
        return ResponseEntity.ok(createdLocation);
    }

    @Operation(summary = "Update a location", description = "Updates an existing location by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the location",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Location.class))),
        @ApiResponse(responseCode = "404", description = "Location not found",
                content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data or referenced entities not found",
                content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(
            @Parameter(description = "ID of the location to update") @PathVariable Long id, 
            @Parameter(description = "Location update request with all required details") @RequestBody UpdateLocationRequest request) {
        try {
            Location location = new Location();
            location.setStreet(request.getName());
            location.setNumber(request.getNumber());

            // Fetch and set Parent Location if provided
            if (request.getParentLocationId() != null) {
                Location parentLocation = locationService.getLocationById(request.getParentLocationId())
                        .orElseThrow(() -> new RuntimeException("Parent Location not found with id " + request.getParentLocationId()));
                location.setParentLocation(parentLocation);
            }

            Location updatedLocation = locationService.updateLocation(id, location);
            return ResponseEntity.ok(updatedLocation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a location", description = "Deletes a location by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the location",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Location not found",
                content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @Parameter(description = "ID of the location to delete") @PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
