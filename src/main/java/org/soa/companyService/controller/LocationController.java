package org.soa.companyService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.soa.companyService.dto.CreateLocationRequest;
import org.soa.companyService.dto.UpdateLocationRequest;
import org.soa.companyService.exception.ErrorResponse;
import org.soa.companyService.model.Location;
import org.soa.companyService.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/locations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Location", description = "Location management API")
@Validated
public class LocationController {

    @Autowired private LocationService locationService;

    @Operation(summary = "Get all locations")
    @ApiResponse(responseCode = "200", description = "List of locations",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Location.class))))
    @GetMapping
    public List<Location> getAllLocations() { return locationService.getAllLocations(); }

    @Operation(summary = "Get location by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location found",
                    content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "404", description = "Location not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(
            @Parameter(description = "Location ID", example = "1") @PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Create location")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Location created",
                    content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = CreateLocationRequest.class),
                    examples = @ExampleObject(value = "{\"name\":\"Trg Leona\",\"number\":\"3\",\"parentLocationId\":1}")))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Location> createLocation(@Valid @RequestBody CreateLocationRequest request) {
        Location location = new Location();
        location.setStreet(request.getName());
        location.setNumber(request.getNumber());
        if (request.getParentLocationId() != null) {
            Location parent = locationService.getLocationById(request.getParentLocationId())
                    .orElseThrow(() -> new RuntimeException("Parent Location not found with id " + request.getParentLocationId()));
            location.setParentLocation(parent);
        }
        Location created = locationService.createLocation(location);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update location")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location updated",
                    content = @Content(schema = @Schema(implementation = Location.class))),
            @ApiResponse(responseCode = "404", description = "Location not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
            content = @Content(schema = @Schema(implementation = UpdateLocationRequest.class),
                    examples = @ExampleObject(value = "{\"name\":\"Nova ulica\",\"number\":\"12\"}")))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Location> updateLocation(
            @Parameter(description = "Location ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateLocationRequest request) {
        Location loc = new Location();
        loc.setStreet(request.getName());
        loc.setNumber(request.getNumber());
        if (request.getParentLocationId() != null) {
            Location parent = locationService.getLocationById(request.getParentLocationId())
                    .orElseThrow(() -> new RuntimeException("Parent Location not found with id " + request.getParentLocationId()));
            loc.setParentLocation(parent);
        }
        Location updated = locationService.updateLocation(id, loc);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete location")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Location deleted"),
            @ApiResponse(responseCode = "404", description = "Location not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @Parameter(description = "Location ID", example = "1") @PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
