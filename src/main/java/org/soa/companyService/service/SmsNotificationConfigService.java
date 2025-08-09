package org.soa.companyService.service;

import org.soa.companyService.model.SmsNotificationConfig;
import org.soa.companyService.repository.SmsNotificationConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmsNotificationConfigService {

    @Autowired
    private SmsNotificationConfigRepository smsNotificationConfigRepository;

    // Get all SMS notification configurations
    public List<SmsNotificationConfig> getAllSmsNotificationConfigs() {
        return smsNotificationConfigRepository.findAll();
    }

    // Get an SMS notification configuration by ID
    public Optional<SmsNotificationConfig> getSmsNotificationConfigById(Long id) {
        return smsNotificationConfigRepository.findById(id);
    }

    // Create a new SMS notification configuration
    public SmsNotificationConfig createSmsNotificationConfig(SmsNotificationConfig smsNotificationConfig) {
        return smsNotificationConfigRepository.save(smsNotificationConfig);
    }

    // Update an existing SMS notification configuration
    public SmsNotificationConfig updateSmsNotificationConfig(Long id, SmsNotificationConfig updatedConfig) {
        return smsNotificationConfigRepository.findById(id)
                .map(existingConfig -> {
                    existingConfig.setName(updatedConfig.getName());
                    return smsNotificationConfigRepository.save(existingConfig);
                })
                .orElseThrow(() -> new RuntimeException("SmsNotificationConfig not found with id " + id));
    }

    // Delete an SMS notification configuration by ID
    public void deleteSmsNotificationConfig(Long id) {
        smsNotificationConfigRepository.deleteById(id);
    }
}
