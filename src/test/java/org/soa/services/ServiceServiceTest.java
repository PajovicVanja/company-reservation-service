//package org.soa.services;
//
//import org.junit.jupiter.api.Test;
//import org.soa.companyService.model.Company;
//import org.soa.companyService.model.ServiceCategory;
//import org.soa.companyService.model.ServiceM;
//import org.soa.companyService.service.CompanyService;
//import org.soa.companyService.service.ServiceCategoryService;
//import org.soa.companyService.service.ServiceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class ServiceServiceTest {
//
//    @Autowired
//    private ServiceService serviceService;
//
//    @Autowired
//    private ServiceCategoryService serviceCategoryService;
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Test
//    public void testCreateService() {
//        // Create dependencies
//        Company company = new Company();
//        company.setDescription("Service Test Company");
//        company.setCompanyName("Service Co");
//        Company savedCompany = companyService.createCompany(company);
//
//        ServiceCategory category = new ServiceCategory();
//        category.setName("Test Category");
//        category.setCompany(savedCompany);
//        ServiceCategory savedCategory = serviceCategoryService.createServiceCategory(category);
//
//        // Create service
//        ServiceM service = new ServiceM();
//        service.setName("Test Service");
//        service.setCategory(savedCategory);
//        service.setCompany(savedCompany);
//        service.setPrice(49.99f);
//        ServiceM savedService = serviceService.createService(service);
//
//        assertThat(savedService.getId()).isNotNull();
//        assertThat(savedService.getName()).isEqualTo("Test Service");
//    }
//
//    @Test
//    public void testUpdateService() {
//        // Create a service
//        ServiceM service = new ServiceM();
//        service.setName("Old Service");
//        service.setPrice(99.99f);
//        ServiceM savedService = serviceService.createService(service);
//
//        // Update the service
//        savedService.setName("Updated Service");
//        savedService.setPrice(199.99f);
//        ServiceM updatedService = serviceService.updateService(savedService.getId(), savedService);
//
//        assertThat(updatedService.getName()).isEqualTo("Updated Service");
//        assertThat(updatedService.getPrice()).isEqualTo(199.99f);
//    }
//
//    @Test
//    public void testDeleteService() {
//        // Create a service
//        ServiceM service = new ServiceM();
//        service.setName("Delete Service");
//        ServiceM savedService = serviceService.createService(service);
//
//        serviceService.deleteService(savedService.getId());
//
//        Optional<ServiceM> retrievedService = serviceService.getServiceById(savedService.getId());
//        assertThat(retrievedService).isEmpty();
//    }
//}
