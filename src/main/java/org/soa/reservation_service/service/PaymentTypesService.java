package org.soa.reservation_service.service;

import org.soa.reservation_service.exception.NotFoundException;
import org.soa.reservation_service.model.PaymentTypes;
import org.soa.reservation_service.repository.PaymentTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentTypesService {

    @Autowired
    private PaymentTypesRepository paymentTypesRepository;

    public List<PaymentTypes> getAllPaymentTypes() {
        return paymentTypesRepository.findAll();
    }

    public Optional<PaymentTypes> getPaymentTypeById(Long id) {
        return paymentTypesRepository.findById(id);
    }

    public PaymentTypes createPaymentType(PaymentTypes paymentType) {
        return paymentTypesRepository.save(paymentType);
    }

    public PaymentTypes updatePaymentType(Long id, PaymentTypes updatedPaymentType) {
        return paymentTypesRepository.findById(id)
                .map(existingPaymentType -> {
                    existingPaymentType.setName(updatedPaymentType.getName());
                    existingPaymentType.setDurationFrom(updatedPaymentType.getDurationFrom());
                    existingPaymentType.setDurationTo(updatedPaymentType.getDurationTo());
                    existingPaymentType.setAddedBy(updatedPaymentType.getAddedBy());
                    existingPaymentType.setDeletedBy(updatedPaymentType.getDeletedBy());
                    return paymentTypesRepository.save(existingPaymentType);
                })
                .orElseThrow(() -> new NotFoundException("PaymentType not found with id " + id));
    }

    public void deletePaymentType(Long id) {
        paymentTypesRepository.deleteById(id);
    }
}
