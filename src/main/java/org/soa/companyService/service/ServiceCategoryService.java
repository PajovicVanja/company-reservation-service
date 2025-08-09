package org.soa.companyService.service;

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

    // Get all service categories
    public List<ServiceCategory> getAllServiceCategories() {
        return serviceCategoryRepository.findAll();
    }

    // Get a service category by ID
    public Optional<ServiceCategory> getServiceCategoryById(Long id) {
        return serviceCategoryRepository.findById(id);
    }

    public List<ServiceCategory> getServiceCategoriesByCompanyUUID(String id) {
        return serviceCategoryRepository.findByCompanyUUID(id);
    }

    // Create a new service category
    public ServiceCategory createServiceCategory(ServiceCategory serviceCategory) {
        return serviceCategoryRepository.save(serviceCategory);
    }

    // Update an existing service category
    public ServiceCategory updateServiceCategory(Long id, ServiceCategory updatedServiceCategory) {
        return serviceCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(updatedServiceCategory.getName());
                    existingCategory.setCompany(updatedServiceCategory.getCompany());
                    return serviceCategoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new RuntimeException("ServiceCategory not found with id " + id));
    }

    // Delete a service category by ID
    public void deleteServiceCategory(Long id) {
        serviceCategoryRepository.deleteById(id);
    }
}
