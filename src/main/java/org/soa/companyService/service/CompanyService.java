package org.soa.companyService.service;

import org.soa.companyService.model.Company;
import org.soa.companyService.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private S3ClientService s3ClientService;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        return companyRepository.findById(id)
                .map(company -> {
                    // Update fields
                    company.setDescription(updatedCompany.getDescription());
                    company.setSmsNotificationConfig(updatedCompany.getSmsNotificationConfig());
                    company.setIdPicture(updatedCompany.getIdPicture());
                    company.setIdAuthUser(updatedCompany.getIdAuthUser());
                    company.setUuidUrl(updatedCompany.getUuidUrl());
                    company.setAddress(updatedCompany.getAddress());
                    company.setPhoneNumber(updatedCompany.getPhoneNumber());
                    company.setEmail(updatedCompany.getEmail());
                    company.setFirstName(updatedCompany.getFirstName());
                    company.setLastName(updatedCompany.getLastName());
                    company.setCompanyName(updatedCompany.getCompanyName());
                    company.setLocation(updatedCompany.getLocation());

                    // Save the updated company
                    return companyRepository.save(company);
                })
                .orElseThrow(() -> new RuntimeException("Company not found with id " + id));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public void uploadPicture(Long id, MultipartFile file) {
        companyRepository.findById(id).ifPresent(company -> {
            try {
                String path = s3ClientService.uploadPicture(file, id);
                company.setIdPicture(path); // Update the company with the picture path
                companyRepository.save(company);
            } catch (Exception e) {
                Logger.getLogger(CompanyService.class.getName()).log(Level.SEVERE, "Failed to upload picture for company with id " + id, e);
                throw new RuntimeException("Failed to upload picture for company with id " + id, e);
            }
        });
    }
}
