package org.soa.reservation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.reservation_service.dto.StatusCreateRequest;
import org.soa.reservation_service.dto.StatusUpdateRequest;
import org.soa.reservation_service.exception.ErrorResponse;
import org.soa.reservation_service.model.Status;
import org.soa.reservation_service.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/statuses", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Statuses", description = "Manage reservation statuses")
public class StatusController {

    @Autowired private StatusService statusService;

    @Operation(summary = "Get all statuses")
    @ApiResponse(responseCode = "200", description = "List of statuses",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Status.class))))
    @GetMapping
    public List<Status> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @Operation(summary = "Get status by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status found",
                    content = @Content(schema = @Schema(implementation = Status.class))),
            @ApiResponse(responseCode = "404", description = "Status not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(
            @Parameter(description = "Status ID", example = "1") @PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status created",
                    content = @Content(schema = @Schema(implementation = Status.class),
                            examples = @ExampleObject(value = "{\"id\":10,\"name\":\"Created\"}"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = StatusCreateRequest.class),
            examples = @ExampleObject(value = "{\"name\":\"Created\",\"addedBy\":1}")
    ))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> createStatus(@org.springframework.web.bind.annotation.RequestBody StatusCreateRequest request) {
        Status status = new Status();
        status.setName(request.getName());
        status.setAddedBy(request.getAddedBy());

        Status createdStatus = statusService.createStatus(status);
        return ResponseEntity.ok(createdStatus);
    }

    @Operation(summary = "Update a status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated",
                    content = @Content(schema = @Schema(implementation = Status.class))),
            @ApiResponse(responseCode = "404", description = "Status not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = StatusUpdateRequest.class),
            examples = @ExampleObject(value = "{\"name\":\"Confirmed\",\"addedBy\":2}")
    ))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Status> updateStatus(
            @Parameter(description = "Status ID", example = "10") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody StatusUpdateRequest request) {
        try {
            Status status = new Status();
            status.setName(request.getName());
            status.setAddedBy(request.getAddedBy());

            Status updatedStatus = statusService.updateStatus(id, status);
            return ResponseEntity.ok(updatedStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a status")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Status deleted"),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(
            @Parameter(description = "Status ID", example = "10") @PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
