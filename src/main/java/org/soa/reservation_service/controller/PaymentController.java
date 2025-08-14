package org.soa.reservation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.reservation_service.dto.PaymentCreateRequest;
import org.soa.reservation_service.dto.PaymentUpdateRequest;
import org.soa.reservation_service.exception.ErrorResponse;
import org.soa.reservation_service.model.Payment;
import org.soa.reservation_service.service.PaymentService;
import org.soa.reservation_service.service.PaymentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Manage payments")
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private PaymentTypesService paymentTypesService;

    @Operation(summary = "Get all payments", description = "Returns a list of all payments.")
    @ApiResponse(responseCode = "200", description = "List of payments",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Payment.class))))
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @Operation(summary = "Get payment by ID", description = "Returns a single payment by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found",
                    content = @Content(schema = @Schema(implementation = Payment.class),
                            examples = @ExampleObject(value = "{\"id\":10,\"status\":\"PAID\",\"datePaid\":\"2025-01-31T09:15:00.000+00:00\",\"amount\":49.99,\"paymentTypes\":{\"id\":1,\"name\":\"Cash\"}}"))),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(
            @Parameter(description = "Payment ID", example = "10") @PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a payment", description = "Creates a new payment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment created",
                    content = @Content(schema = @Schema(implementation = Payment.class),
                            examples = @ExampleObject(value = "{\"id\":100,\"status\":\"PAID\",\"datePaid\":\"2025-01-31T09:15:00.000+00:00\",\"amount\":49.99,\"paymentTypes\":{\"id\":1}}"))),
            @ApiResponse(responseCode = "500", description = "Failed to resolve payment type or server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = PaymentCreateRequest.class),
            examples = @ExampleObject(value = "{\"paymentTypeId\":1,\"status\":\"PAID\",\"datePaid\":\"2025-01-31T09:15:00.000+00:00\",\"amount\":49.99}")
    ))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> createPayment(@org.springframework.web.bind.annotation.RequestBody PaymentCreateRequest request) {
        Payment payment = new Payment();
        payment.setStatus(request.getStatus());
        payment.setDatePaid(request.getDatePaid());
        payment.setAmount(request.getAmount());

        if (request.getPaymentTypeId() != null) {
            payment.setPaymentTypes(paymentTypesService.getPaymentTypeById(request.getPaymentTypeId())
                    .orElseThrow(() -> new RuntimeException("Payment Type not found with id " + request.getPaymentTypeId())));
        }

        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    @Operation(summary = "Update a payment", description = "Updates an existing payment by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment updated",
                    content = @Content(schema = @Schema(implementation = Payment.class))),
            @ApiResponse(responseCode = "404", description = "Payment or referenced type not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = PaymentUpdateRequest.class),
            examples = @ExampleObject(value = "{\"paymentTypeId\":1,\"status\":\"REFUNDED\",\"datePaid\":\"2025-02-01T12:00:00.000+00:00\",\"amount\":49.99}")
    ))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> updatePayment(
            @Parameter(description = "Payment ID", example = "10") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody PaymentUpdateRequest request) {
        try {
            Payment payment = new Payment();
            payment.setStatus(request.getStatus());
            payment.setDatePaid(request.getDatePaid());
            payment.setAmount(request.getAmount());

            if (request.getPaymentTypeId() != null) {
                payment.setPaymentTypes(paymentTypesService.getPaymentTypeById(request.getPaymentTypeId())
                        .orElseThrow(() -> new RuntimeException("Payment Type not found with id " + request.getPaymentTypeId())));
            }

            Payment updatedPayment = paymentService.updatePayment(id, payment);
            return ResponseEntity.ok(updatedPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a payment", description = "Deletes a payment by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment deleted"),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "Payment ID", example = "10") @PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
