//package org.soa.services;
//
//import org.junit.jupiter.api.Test;
//import org.soa.companyService.model.BusinessHours;
//import org.soa.companyService.model.Company;
//import org.soa.companyService.repository.BusinessHoursRepository;
//import org.soa.companyService.service.BusinessHoursService;
//import org.soa.companyService.service.CompanyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.sql.Time;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class BusinessHoursServiceTest {
//
//    @Autowired
//    private BusinessHoursService businessHoursService;
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Autowired
//    private BusinessHoursRepository businessHoursRepository;
//
//    @Test
//    public void testCreateBusinessHours() {
//        // Create a company dependency
//        Company company = new Company();
//        company.setDescription("Test Company");
//        company.setCompanyName("Test Co");
//        Company savedCompany = companyService.createCompany(company);
//
//        // Create business hours
//        BusinessHours businessHours = new BusinessHours();
//        businessHours.setDayNumber(1);
//        businessHours.setTimeFrom(Time.valueOf("09:00:00"));
//        businessHours.setTimeTo(Time.valueOf("17:00:00"));
//        businessHours.setPauseFrom(Time.valueOf("12:00:00"));
//        businessHours.setPauseTo(Time.valueOf("12:30:00"));
//        businessHours.setDay("Monday");
//        businessHours.setCompany(savedCompany);
//
//        BusinessHours savedBusinessHours = businessHoursService.createBusinessHours(businessHours);
//
//        assertThat(savedBusinessHours.getId()).isNotNull();
//        assertThat(savedBusinessHours.getDay()).isEqualTo("Monday");
//    }
//
//    @Test
//    public void testUpdateBusinessHours() {
//        // Create a business hours entry
//        BusinessHours businessHours = new BusinessHours();
//        businessHours.setDayNumber(1);
//        businessHours.setTimeFrom(Time.valueOf("09:00:00"));
//        businessHours.setTimeTo(Time.valueOf("17:00:00"));
//        businessHours.setDay("Monday");
//        BusinessHours savedBusinessHours = businessHoursService.createBusinessHours(businessHours);
//
//        // Update business hours
//        savedBusinessHours.setDay("Updated Monday");
//        BusinessHours updatedBusinessHours = businessHoursService.updateBusinessHours(savedBusinessHours.getId(), savedBusinessHours);
//
//        assertThat(updatedBusinessHours.getDay()).isEqualTo("Updated Monday");
//    }
//
//    @Test
//    public void testDeleteBusinessHours() {
//        // Create a company dependency
//        Company company = new Company();
//        company.setDescription("Test Company for Delete");
//        company.setCompanyName("Test Delete Co");
//        Company savedCompany = companyService.createCompany(company);
//
//        // Create a business hours entry
//        BusinessHours businessHours = new BusinessHours();
//        businessHours.setDayNumber(2);
//        businessHours.setDay("Tuesday");
//        businessHours.setTimeFrom(Time.valueOf("09:00:00")); // Required field
//        businessHours.setTimeTo(Time.valueOf("17:00:00")); // Required field
//        businessHours.setPauseFrom(Time.valueOf("12:00:00")); // Optional but added for completeness
//        businessHours.setPauseTo(Time.valueOf("12:30:00")); // Optional but added for completeness
//        businessHours.setCompany(savedCompany); // Ensure foreign key reference
//
//        BusinessHours savedBusinessHours = businessHoursService.createBusinessHours(businessHours);
//
//        // Delete the created business hours
//        businessHoursService.deleteBusinessHours(savedBusinessHours.getId());
//
//        // Verify deletion
//        Optional<BusinessHours> retrievedBusinessHours = businessHoursService.getBusinessHoursById(savedBusinessHours.getId());
//        assertThat(retrievedBusinessHours).isEmpty();
//    }
//
//}
