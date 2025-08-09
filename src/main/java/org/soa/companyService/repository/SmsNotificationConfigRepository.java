package org.soa.companyService.repository;

import org.soa.companyService.model.SmsNotificationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsNotificationConfigRepository extends JpaRepository<SmsNotificationConfig, Long> {
}
