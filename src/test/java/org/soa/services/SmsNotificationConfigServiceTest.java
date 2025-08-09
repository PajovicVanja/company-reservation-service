//package org.soa.services;
//
//import org.junit.jupiter.api.Test;
//import org.soa.companyService.model.SmsNotificationConfig;
//import org.soa.companyService.repository.SmsNotificationConfigRepository;
//import org.soa.companyService.service.SmsNotificationConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class SmsNotificationConfigServiceTest {
//
//    @Autowired
//    private SmsNotificationConfigService smsNotificationConfigService;
//
//    @Autowired
//    private SmsNotificationConfigRepository smsNotificationConfigRepository;
//
//    @Test
//    public void testCreateSmsNotificationConfig() {
//        SmsNotificationConfig config = new SmsNotificationConfig();
//        config.setName("Test Notification");
//        SmsNotificationConfig savedConfig = smsNotificationConfigService.createSmsNotificationConfig(config);
//
//        assertThat(savedConfig.getId()).isNotNull();
//        assertThat(savedConfig.getName()).isEqualTo("Test Notification");
//    }
//
//    @Test
//    public void testGetSmsNotificationConfigById() {
//        SmsNotificationConfig config = new SmsNotificationConfig();
//        config.setName("Retrieve Test Notification");
//        SmsNotificationConfig savedConfig = smsNotificationConfigService.createSmsNotificationConfig(config);
//
//        Optional<SmsNotificationConfig> retrievedConfig = smsNotificationConfigService.getSmsNotificationConfigById(savedConfig.getId());
//        assertThat(retrievedConfig).isPresent();
//        assertThat(retrievedConfig.get().getName()).isEqualTo("Retrieve Test Notification");
//    }
//
//    @Test
//    public void testUpdateSmsNotificationConfig() {
//        SmsNotificationConfig config = new SmsNotificationConfig();
//        config.setName("Old Name");
//        SmsNotificationConfig savedConfig = smsNotificationConfigService.createSmsNotificationConfig(config);
//
//        savedConfig.setName("Updated Name");
//        SmsNotificationConfig updatedConfig = smsNotificationConfigService.updateSmsNotificationConfig(savedConfig.getId(), savedConfig);
//
//        assertThat(updatedConfig.getName()).isEqualTo("Updated Name");
//    }
//
//    @Test
//    public void testDeleteSmsNotificationConfig() {
//        SmsNotificationConfig config = new SmsNotificationConfig();
//        config.setName("Delete Test Notification");
//        SmsNotificationConfig savedConfig = smsNotificationConfigService.createSmsNotificationConfig(config);
//
//        smsNotificationConfigService.deleteSmsNotificationConfig(savedConfig.getId());
//
//        Optional<SmsNotificationConfig> retrievedConfig = smsNotificationConfigService.getSmsNotificationConfigById(savedConfig.getId());
//        assertThat(retrievedConfig).isEmpty();
//    }
//}
