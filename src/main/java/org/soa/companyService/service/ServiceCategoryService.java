package org.soa.companyService.service;

import org.soa.companyService.exception.NotFoundException;
import org.soa.companyService.model.ServiceCategory;
import org.soa.companyService.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryRepository.findAll();
    }

    public Optional<ServiceCategory> getServiceCategoryById(Long id) {
        return serviceCategoryRepository.findById(id);
    }

    public List<ServiceCategory> getServiceCategoriesByCompanyUUID(String id) {
        return serviceCategoryRepository.findByCompanyUUID(id);
    }

    public ServiceCategory createServiceCategory(ServiceCategory serviceCategory) {
        return serviceCategoryRepository.save(serviceCategory);
    }

    public ServiceCategory updateServiceCategory(Long id, ServiceCategory updatedServiceCategory) {
        return serviceCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(updatedServiceCategory.getName());
                    existingCategory.setCompany(updatedServiceCategory.getCompany());
                    return serviceCategoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new NotFoundException("ServiceCategory not found with id " + id));
    }

    public void deleteServiceCategory(Long id) {
        serviceCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("ServiceCategory not found with id " + id));
        serviceCategoryRepository.deleteById(id);
    }
}
