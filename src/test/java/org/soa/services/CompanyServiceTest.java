//package org.soa.services;
//
//import org.junit.jupiter.api.Test;
//import org.soa.companyService.model.Company;
//import org.soa.companyService.model.Location;
//import org.soa.companyService.model.SmsNotificationConfig;
//import org.soa.companyService.repository.CompanyRepository;
//import org.soa.companyService.service.CompanyService;
//import org.soa.companyService.service.LocationService;
//import org.soa.companyService.service.SmsNotificationConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class CompanyServiceTest {
//
//    @Autowired
//    private CompanyService companyService;
//
//    @Autowired
//    private SmsNotificationConfigService smsNotificationConfigService;
//
//    @Autowired
//    private LocationService locationService;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Test
//    public void testCreateCompany() {
//        // Create dependencies
//        SmsNotificationConfig notification = new SmsNotificationConfig();
//        notification.setName("Notification for Company");
//        SmsNotificationConfig savedNotification = smsNotificationConfigService.createSmsNotificationConfig(notification);
//
//        Location location = new Location();
//        location.setStreet("Ulica");
//        location.setNumber("12G");
//        Location savedLocation = locationService.createLocation(location);
//
//        // Create company
//        Company company = new Company();
//        company.setDescription("Test Company");
//        company.setSmsNotificationConfig(savedNotification);
//        company.setLocation(savedLocation);
//        company.setCompanyName("Test Company Name");
//
//        Company savedCompany = companyService.createCompany(company);
//
//        assertThat(savedCompany.getId()).isNotNull();
//        assertThat(savedCompany.getDescription()).isEqualTo("Test Company");
//        assertThat(savedCompany.getSmsNotificationConfig().getId()).isEqualTo(savedNotification.getId());
//        assertThat(savedCompany.getLocation().getId()).isEqualTo(savedLocation.getId());
//    }
//
//    @Test
//    public void testUpdateCompany() {
//        // Create dependencies
//        SmsNotificationConfig notification = new SmsNotificationConfig();
//        notification.setName("Initial Notification");
//        SmsNotificationConfig savedNotification = smsNotificationConfigService.createSmsNotificationConfig(notification);
//
//        Location location = new Location();
//        location.setStreet("Initial Location");
//        location.setNumber("100");
//        Location savedLocation = locationService.createLocation(location);
//
//        // Create company
//        Company company = new Company();
//        company.setDescription("Initial Description");
//        company.setSmsNotificationConfig(savedNotification);
//        company.setLocation(savedLocation);
//        company.setCompanyName("Initial Company");
//        Company savedCompany = companyService.createCompany(company);
//
//        // Update company
//        savedCompany.setDescription("Updated Description");
//        savedCompany.setCompanyName("Updated Company Name");
//        Company updatedCompany = companyService.updateCompany(savedCompany.getId(), savedCompany);
//
//        assertThat(updatedCompany.getDescription()).isEqualTo("Updated Description");
//        assertThat(updatedCompany.getCompanyName()).isEqualTo("Updated Company Name");
//    }
//
//    @Test
//    public void testDeleteCompany() {
//        // Create dependencies
//        SmsNotificationConfig notification = new SmsNotificationConfig();
//        notification.setName("Notification to Delete");
//        SmsNotificationConfig savedNotification = smsNotificationConfigService.createSmsNotificationConfig(notification);
//
//        Location location = new Location();
//        location.setStreet("Location to Delete");
//        location.setNumber("200");
//        Location savedLocation = locationService.createLocation(location);
//
//        // Create company
//        Company company = new Company();
//        company.setDescription("Delete This Company");
//        company.setSmsNotificationConfig(savedNotification);
//        company.setLocation(savedLocation);
//        company.setCompanyName("Delete Company Name");
//        Company savedCompany = companyService.createCompany(company);
//
//        // Delete company
//        companyService.deleteCompany(savedCompany.getId());
//
//        Optional<Company> retrievedCompany = companyService.getCompanyById(savedCompany.getId());
//        assertThat(retrievedCompany).isEmpty();
//    }
//}
