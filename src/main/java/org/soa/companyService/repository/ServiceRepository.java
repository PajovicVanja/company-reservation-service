package org.soa.companyService.repository;

import org.soa.companyService.model.ServiceM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceM, Long> {

    @Query("SELECT s FROM ServiceM s WHERE s.company.id = :companyId")
    List<ServiceM> findByCompanyId(Long companyId);

    @Query("SELECT s FROM ServiceM s WHERE s.company.id = :companyId and s.category.id = :categoryId")
    List<ServiceM> findByCompanyIdAndCategory(Long companyId, Long categoryId);


}
