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

    public List<SmsNotificationConfig> getAllSmsNotificationConfigs() {
        return smsNotificationConfigRepository.findAll();
    }

    public Optional<SmsNotificationConfig> getSmsNotificationConfigById(Long id) {
        return smsNotificationConfigRepository.findById(id);
    }

    public SmsNotificationConfig createSmsNotificationConfig(SmsNotificationConfig smsNotificationConfig) {
        return smsNotificationConfigRepository.save(smsNotificationConfig);
    }

    public SmsNotificationConfig updateSmsNotificationConfig(Long id, SmsNotificationConfig updatedConfig) {
        return smsNotificationConfigRepository.findById(id)
                .map(existingConfig -> {
                    if (updatedConfig.getName() != null) {
                        existingConfig.setName(updatedConfig.getName());
                    }
                    if (updatedConfig.getNotificationMessage() != null) {
                        existingConfig.setNotificationMessage(updatedConfig.getNotificationMessage());
                    }
                    return smsNotificationConfigRepository.save(existingConfig);
                })
                .orElseThrow(() -> new RuntimeException("SmsNotificationConfig not found with id " + id));
    }

    public void deleteSmsNotificationConfig(Long id) {
        smsNotificationConfigRepository.deleteById(id);
    }
}
