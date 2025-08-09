package org.soa.reservation_service.service;

import org.soa.reservation_service.model.Payment;
import org.soa.reservation_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get a payment by ID
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    // Create a new payment
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // Update an existing payment
    public Payment updatePayment(Long id, Payment updatedPayment) {
        return paymentRepository.findById(id)
                .map(existingPayment -> {
                    existingPayment.setPaymentTypes(updatedPayment.getPaymentTypes());
                    existingPayment.setStatus(updatedPayment.getStatus());
                    existingPayment.setDatePaid(updatedPayment.getDatePaid());
                    existingPayment.setAmount(updatedPayment.getAmount());
                    return paymentRepository.save(existingPayment);
                })
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + id));
    }

    // Delete a payment by ID
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
