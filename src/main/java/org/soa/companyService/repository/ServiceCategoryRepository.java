package org.soa.companyService.repository;

import org.soa.companyService.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    @Query("SELECT sc FROM ServiceCategory sc WHERE sc.company.uuidUrl = :companyId")
    List<ServiceCategory> findByCompanyUUID(String companyId);
}
