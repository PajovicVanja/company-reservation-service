package org.soa.companyService.service;

import org.soa.companyService.model.BusinessHours;
import org.soa.companyService.repository.BusinessHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessHoursService {

    @Autowired
    private BusinessHoursRepository businessHoursRepository;

    // Get all business hours
    public List<BusinessHours> getAllBusinessHours() {
        return businessHoursRepository.findAll();
    }

    // Get business hours by ID
    public Optional<BusinessHours> getBusinessHoursById(Long id) {
        return businessHoursRepository.findById(id);
    }

    public List<BusinessHours> getBusinessHoursByCompanyId(Long id) {
        return businessHoursRepository.findByCompanyId(id);
    }

    // Create new business hours
    public BusinessHours createBusinessHours(BusinessHours businessHours) {
        return businessHoursRepository.save(businessHours);
    }

    // Update existing business hours
    public BusinessHours updateBusinessHours(Long id, BusinessHours updatedBusinessHours) {
        return businessHoursRepository.findById(id)
                .map(existingBusinessHours -> {
                    existingBusinessHours.setDayNumber(updatedBusinessHours.getDayNumber());
                    existingBusinessHours.setTimeFrom(updatedBusinessHours.getTimeFrom());
                    existingBusinessHours.setTimeTo(updatedBusinessHours.getTimeTo());
                    existingBusinessHours.setPauseFrom(updatedBusinessHours.getPauseFrom());
                    existingBusinessHours.setPauseTo(updatedBusinessHours.getPauseTo());
                    existingBusinessHours.setCompany(updatedBusinessHours.getCompany());
                    existingBusinessHours.setDay(updatedBusinessHours.getDay());
                    return businessHoursRepository.save(existingBusinessHours);
                })
                .orElseThrow(() -> new RuntimeException("BusinessHours not found with id " + id));
    }

    // Delete business hours by ID
    public void deleteBusinessHours(Long id) {
        businessHoursRepository.deleteById(id);
    }
}
