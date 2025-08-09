//package org.soa.services;
//
//import org.junit.jupiter.api.Test;
//import org.soa.companyService.model.Company;
//import org.soa.companyService.model.ServiceCategory;
//import org.soa.companyService.service.CompanyService;
//import org.soa.companyService.service.ServiceCategoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class ServiceCategoryServiceTest {
//
//    @Autowired
//    private ServiceCategoryService serviceCategoryService;
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Test
//    public void testCreateServiceCategory() {
//        // Create a company dependency
//        Company company = new Company();
//        company.setDescription("ServiceCategory Test Company");
//        company.setCompanyName("ServiceCategory Co");
//        Company savedCompany = companyService.createCompany(company);
//
//        // Create service category
//        ServiceCategory serviceCategory = new ServiceCategory();
//        serviceCategory.setName("Test Category");
//        serviceCategory.setCompany(savedCompany);
//
//        ServiceCategory savedCategory = serviceCategoryService.createServiceCategory(serviceCategory);
//
//        assertThat(savedCategory.getId()).isNotNull();
//        assertThat(savedCategory.getName()).isEqualTo("Test Category");
//    }
//
//    @Test
//    public void testUpdateServiceCategory() {
//        // Create a service category
//        ServiceCategory serviceCategory = new ServiceCategory();
//        serviceCategory.setName("Old Category");
//        ServiceCategory savedCategory = serviceCategoryService.createServiceCategory(serviceCategory);
//
//        // Update the category
//        savedCategory.setName("Updated Category");
//        ServiceCategory updatedCategory = serviceCategoryService.updateServiceCategory(savedCategory.getId(), savedCategory);
//
//        assertThat(updatedCategory.getName()).isEqualTo("Updated Category");
//    }
//
//    @Test
//    public void testDeleteServiceCategory() {
//        // Create a service category
//        ServiceCategory serviceCategory = new ServiceCategory();
//        serviceCategory.setName("Delete Category");
//        ServiceCategory savedCategory = serviceCategoryService.createServiceCategory(serviceCategory);
//
//        serviceCategoryService.deleteServiceCategory(savedCategory.getId());
//
//        Optional<ServiceCategory> retrievedCategory = serviceCategoryService.getServiceCategoryById(savedCategory.getId());
//        assertThat(retrievedCategory).isEmpty();
//    }
//}
