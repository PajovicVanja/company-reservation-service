package org.soa.reservation_service.repository;

import org.soa.reservation_service.model.PaymentTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypesRepository extends JpaRepository<PaymentTypes, Long> {
}