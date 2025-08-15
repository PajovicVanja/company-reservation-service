package org.soa.reservation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.reservation_service.dto.*;
import org.soa.reservation_service.exception.ErrorResponse;
import org.soa.reservation_service.model.Reservation;
import org.soa.reservation_service.service.PaymentService;
import org.soa.reservation_service.service.ReservationService;
import org.soa.reservation_service.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservations", description = "Create and manage reservations")
public class ReservationController {

    @Autowired private ReservationService reservationService;
    @Autowired private StatusService statusService;
    @Autowired private PaymentService paymentService;

    @Operation(summary = "List reservations for a company")
    @ApiResponse(responseCode = "200", description = "List of reservations",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Reservation.class))))
    @GetMapping("/company/{idCompany}")
    public List<Reservation> getAllReservationsByCompany(
            @Parameter(description = "Company ID", example = "42") @PathVariable Long idCompany) {
        return reservationService.getAllReservationsByCompany(idCompany);
    }

    @Operation(summary = "List reservations for a user")
    @ApiResponse(responseCode = "200", description = "List of reservations",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Reservation.class))))
    @GetMapping("/user/{idUser}")
    public List<Reservation> getAllReservationsByUser(
            @Parameter(description = "User ID", example = "77") @PathVariable Long idUser) {
        return reservationService.getAllReservationsByUser(idUser);
    }

    @Operation(summary = "Get reservation by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation found",
                    content = @Content(schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @Parameter(description = "Reservation ID", example = "10") @PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a reservation",
            description = "Creates a reservation. Required: date, idService, paymentId. Optional: idCompany, employeeId, customer details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation created",
                    content = @Content(schema = @Schema(implementation = Reservation.class),
                            examples = @ExampleObject(value = "{\"id\":100,\"date\":\"2025-02-01T10:00:00.000+00:00\",\"dateTo\":\"2025-02-01T11:00:00.000+00:00\",\"idCustomer\":123,\"customerFullName\":\"John Doe\",\"status\":{\"id\":1}}"))),
            @ApiResponse(responseCode = "500", description = "Invalid service/payment or server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = ReservationCreateRequest.class),
            examples = @ExampleObject(value = "{\"date\":\"2025-02-01T10:00:00.000+00:00\",\"idCompany\":42,\"idService\":7,\"paymentId\":5,\"employeeId\":321,\"customerFullName\":\"John Doe\",\"customerPhoneNumber\":38640111222}")
    ))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> createReservation(@org.springframework.web.bind.annotation.RequestBody ReservationCreateRequest request) {
        Reservation createdReservation = reservationService.createReservation(request);
        return ResponseEntity.ok(createdReservation);
    }

    @Operation(summary = "Find free slots for a company / service",
            description = "Returns a list of available HH:mm - HH:mm slots on the given day.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of free slots",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)),
                            examples = @ExampleObject(value = "[\"09:00 - 10:00\",\"10:00 - 11:00\"]"))),
            @ApiResponse(responseCode = "500", description = "Service not found or server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/free-slots/{idCompany}")
    public ResponseEntity<List<String>> getFreeSlots(
            @Parameter(description = "Company ID", example = "42") @PathVariable Long idCompany,
            @Parameter(description = "Date (start of search window)", example = "2025-02-01T00:00:00") @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @Parameter(description = "Service ID", example = "7") @RequestParam("serviceId") Long serviceId,
            @Parameter(description = "Employee ID (optional)", example = "321") @RequestParam(value = "employeeId", required = false) Long employeeId) {

        List<String> list = reservationService.getFreeSlots(idCompany, date, serviceId, employeeId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Confirm reservation with 2FA code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation confirmed",
                    content = @Content(schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Reservation not found or invalid 2FA",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}/confirm/{twoFACode}")
    public ResponseEntity<Reservation> confirmReservation(
            @Parameter(example = "10") @PathVariable Long id,
            @Parameter(example = "123456") @PathVariable Long twoFACode) {
        try {
            Reservation updatedReservation = reservationService.confirmReservation(id, twoFACode);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update reservation status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated",
                    content = @Content(schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Reservation or status not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = ReservationStatusUpdateRequest.class),
            examples = @ExampleObject(value = "{\"statusId\":2}")
    ))
    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(
            @Parameter(example = "10") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody ReservationStatusUpdateRequest request) {
        try {
            Reservation updatedReservation = reservationService.updateReservationStatus(id, request.getStatusId());
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Admin update (hide) reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation updated (hidden)",
                    content = @Content(schema = @Schema(implementation = Reservation.class))),
            @ApiResponse(responseCode = "404", description = "Reservation or status not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = AdminReservationUpdateRequest.class),
            examples = @ExampleObject(value = "{\"statusId\":3}")
    ))
    @PutMapping("/admin/{id}")
    public ResponseEntity<Reservation> adminUpdateReservation(
            @Parameter(example = "10") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody AdminReservationUpdateRequest request) {
        try {
            Reservation updatedReservation = reservationService.adminUpdateReservation(id, request.getStatusId());
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete (cancel) reservation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reservation canceled"),
            @ApiResponse(responseCode = "404", description = "Reservation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(example = "10") @PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Employee Service calls: GET {RESERVATION_SERVICE_URL}/reservations?employee_id={employee_id}
    @Operation(summary = "List reservations by employee (for Employee Service proxy)")
    @ApiResponse(responseCode = "200", description = "List of reservations",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Reservation.class))))
    @GetMapping(params = "employee_id")
    public List<Reservation> getAllReservationsByEmployee(
            @Parameter(description = "Employee ID", example = "321") @RequestParam("employee_id") Long employeeId) {
        return reservationService.getReservationsByEmployee(employeeId);
    }
}
