package org.soa.reservation_service.service;

import org.soa.reservation_service.exception.NotFoundException;
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

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(Long id, Payment updatedPayment) {
        return paymentRepository.findById(id)
                .map(existingPayment -> {
                    existingPayment.setPaymentTypes(updatedPayment.getPaymentTypes());
                    existingPayment.setStatus(updatedPayment.getStatus());
                    existingPayment.setDatePaid(updatedPayment.getDatePaid());
                    existingPayment.setAmount(updatedPayment.getAmount());
                    return paymentRepository.save(existingPayment);
                })
                .orElseThrow(() -> new NotFoundException("Payment not found with id " + id));
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
