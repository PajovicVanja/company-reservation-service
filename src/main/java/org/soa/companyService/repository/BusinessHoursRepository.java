package org.soa.companyService.repository;

import org.soa.companyService.model.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {

    @Query("SELECT bh FROM BusinessHours bh WHERE bh.company.id = :companyId")
    List<BusinessHours> findByCompanyId(Long companyId);
}



