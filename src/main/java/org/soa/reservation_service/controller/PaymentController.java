package org.soa.reservation_service.controller;

import org.soa.reservation_service.dto.PaymentCreateRequest;
import org.soa.reservation_service.dto.PaymentUpdateRequest;
import org.soa.reservation_service.model.Payment;
import org.soa.reservation_service.service.PaymentService;
import org.soa.reservation_service.service.PaymentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentTypesService paymentTypesService;

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentCreateRequest request) {
        Payment payment = new Payment();
        payment.setStatus(request.getStatus());
        payment.setDatePaid(request.getDatePaid());
        payment.setAmount(request.getAmount());

        // Fetch and set Payment Type if provided
        if (request.getPaymentTypeId() != null) {
            payment.setPaymentTypes(paymentTypesService.getPaymentTypeById(request.getPaymentTypeId())
                    .orElseThrow(() -> new RuntimeException("Payment Type not found with id " + request.getPaymentTypeId())));
        }

        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody PaymentUpdateRequest request) {
        try {
            Payment payment = new Payment();
            payment.setStatus(request.getStatus());
            payment.setDatePaid(request.getDatePaid());
            payment.setAmount(request.getAmount());

            // Fetch and set Payment Type if provided
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
