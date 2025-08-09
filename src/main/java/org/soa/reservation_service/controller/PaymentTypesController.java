package org.soa.reservation_service.controller;

import org.soa.reservation_service.dto.PaymentTypesCreateRequest;
import org.soa.reservation_service.dto.PaymentTypesUpdateRequest;
import org.soa.reservation_service.model.PaymentTypes;
import org.soa.reservation_service.service.PaymentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-types")
public class PaymentTypesController {

    @Autowired
    private PaymentTypesService paymentTypesService;

    @GetMapping
    public List<PaymentTypes> getAllPaymentTypes() {
        return paymentTypesService.getAllPaymentTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentTypes> getPaymentTypeById(@PathVariable Long id) {
        return paymentTypesService.getPaymentTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentTypes> createPaymentType(@RequestBody PaymentTypesCreateRequest request) {
        PaymentTypes paymentType = new PaymentTypes();
        paymentType.setName(request.getName());
        paymentType.setDurationFrom(request.getDurationFrom());
        paymentType.setDurationTo(request.getDurationTo());
        paymentType.setAddedBy(request.getAddedBy());
        paymentType.setDeletedBy(request.getDeletedBy());

        PaymentTypes createdPaymentType = paymentTypesService.createPaymentType(paymentType);
        return ResponseEntity.ok(createdPaymentType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentTypes> updatePaymentType(@PathVariable Long id, @RequestBody PaymentTypesUpdateRequest request) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentType(@PathVariable Long id) {
        paymentTypesService.deletePaymentType(id);
        return ResponseEntity.noContent().build();
    }
}
