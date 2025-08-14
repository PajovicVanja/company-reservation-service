package org.soa.reservation_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.soa.reservation_service.dto.PaymentTypesCreateRequest;
import org.soa.reservation_service.dto.PaymentTypesUpdateRequest;
import org.soa.reservation_service.exception.ErrorResponse;
import org.soa.reservation_service.model.PaymentTypes;
import org.soa.reservation_service.service.PaymentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/payment-types", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment Types", description = "Manage payment types")
public class PaymentTypesController {

    @Autowired private PaymentTypesService paymentTypesService;

    @Operation(summary = "Get all payment types")
    @ApiResponse(responseCode = "200", description = "List of payment types",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = PaymentTypes.class))))
    @GetMapping
    public List<PaymentTypes> getAllPaymentTypes() {
        return paymentTypesService.getAllPaymentTypes();
    }

    @Operation(summary = "Get payment type by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment type found",
                    content = @Content(schema = @Schema(implementation = PaymentTypes.class))),
            @ApiResponse(responseCode = "404", description = "Payment type not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypes> getPaymentTypeById(
            @Parameter(description = "Payment type ID", example = "1") @PathVariable Long id) {
        return paymentTypesService.getPaymentTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a payment type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment type created",
                    content = @Content(schema = @Schema(implementation = PaymentTypes.class),
                            examples = @ExampleObject(value = "{\"id\":100,\"name\":\"Cash\"}"))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = PaymentTypesCreateRequest.class),
            examples = @ExampleObject(value = "{\"name\":\"Cash\",\"durationFrom\":null,\"durationTo\":null,\"addedBy\":1}")
    ))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentTypes> createPaymentType(@org.springframework.web.bind.annotation.RequestBody PaymentTypesCreateRequest request) {
        PaymentTypes paymentType = new PaymentTypes();
        paymentType.setName(request.getName());
        paymentType.setDurationFrom(request.getDurationFrom());
        paymentType.setDurationTo(request.getDurationTo());
        paymentType.setAddedBy(request.getAddedBy());
        paymentType.setDeletedBy(request.getDeletedBy());

        PaymentTypes createdPaymentType = paymentTypesService.createPaymentType(paymentType);
        return ResponseEntity.ok(createdPaymentType);
    }

    @Operation(summary = "Update a payment type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment type updated",
                    content = @Content(schema = @Schema(implementation = PaymentTypes.class))),
            @ApiResponse(responseCode = "404", description = "Payment type not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestBody(required = true, content = @Content(
            schema = @Schema(implementation = PaymentTypesUpdateRequest.class),
            examples = @ExampleObject(value = "{\"name\":\"Card\",\"durationFrom\":null,\"durationTo\":null,\"addedBy\":2}")
    ))
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentTypes> updatePaymentType(
            @Parameter(description = "Payment type ID", example = "1") @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody PaymentTypesUpdateRequest request) {
        try {
            PaymentTypes paymentType = new PaymentTypes();
            paymentType.setName(request.getName());
            paymentType.setDurationFrom(request.getDurationFrom());
            paymentType.setDurationTo(request.getDurationTo());
            paymentType.setAddedBy(request.getAddedBy());
            paymentType.setDeletedBy(request.getDeletedBy());

            PaymentTypes updatedPaymentType = paymentTypesService.updatePaymentType(id, paymentType);
            return ResponseEntity.ok(updatedPaymentType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a payment type")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Payment type deleted"),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentType(
            @Parameter(description = "Payment type ID", example = "1") @PathVariable Long id) {
        paymentTypesService.deletePaymentType(id);
        return ResponseEntity.noContent().build();
    }
}
