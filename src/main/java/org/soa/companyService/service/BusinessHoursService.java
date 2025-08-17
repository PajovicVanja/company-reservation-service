package org.soa.companyService.service;

import org.soa.companyService.exception.NotFoundException;
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

    public List<BusinessHours> getAllBusinessHours() {
        return businessHoursRepository.findAll();
    }

    public Optional<BusinessHours> getBusinessHoursById(Long id) {
        return businessHoursRepository.findById(id);
    }

    public List<BusinessHours> getBusinessHoursByCompanyId(Long id) {
        return businessHoursRepository.findByCompanyId(id);
    }

    public BusinessHours createBusinessHours(BusinessHours businessHours) {
        return businessHoursRepository.save(businessHours);
    }

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
                .orElseThrow(() -> new NotFoundException("BusinessHours not found with id " + id));
    }

    public void deleteBusinessHours(Long id) {
        businessHoursRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BusinessHours not found with id " + id));
        businessHoursRepository.deleteById(id);
    }
}
