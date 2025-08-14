package org.soa.companyService.service;

import com.google.gson.GsonBuilder;
import org.soa.companyService.model.Company;
import org.soa.companyService.repository.CompanyRepository;
import org.soa.reservation_service.client.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @Autowired
    private AdminClient adminClient;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Company createCompany(Company company) {
        adminClient.createCompany(company);
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        try {
            updateCompanyOnAdminService(id, updatedCompany);
        } catch (IOException | InterruptedException ignored) {}
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
        try {
            deleteCompanyOnAdminService(id);
        } catch (IOException | InterruptedException ignored) {}
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

    private void createCompanyOnAdminService(Company company) throws IOException, InterruptedException {
        // This method should call the administrator service to create a company
        // For now, we will just log the action
        Logger.getLogger(CompanyService.class.getName()).log(Level.INFO, "Creating company on admin service: {0}", company);
        AdminCompanyRequest adminCompanyRequest = new AdminCompanyRequest(
                company.getCompanyName(),
                company.getFirstName() + " " + company.getLastName(),
                company.getEmail(),
                company.getDescription(),
                company.getPhoneNumber(),
                company.getAddress(),
                company.getLocation().getStreet()
        );
        GsonBuilder gsonBuilder = new GsonBuilder();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create( System.getenv("admin_url") + "/api/v1/companies"))
                .POST(HttpRequest.BodyPublishers.ofString(gsonBuilder.create().toJson(adminCompanyRequest)))
                .header("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void updateCompanyOnAdminService(Long id, Company company) throws IOException, InterruptedException {
        // This method should call the administrator service to create a company
        // For now, we will just log the action
        Logger.getLogger(CompanyService.class.getName()).log(Level.INFO, "Creating company on admin service: {0}", company);
        AdminCompanyRequest adminCompanyRequest = new AdminCompanyRequest(
                company.getCompanyName(),
                company.getFirstName() + " " + company.getLastName(),
                company.getEmail(),
                company.getDescription(),
                company.getPhoneNumber(),
                company.getAddress(),
                company.getLocation().getStreet()
        );
        GsonBuilder gsonBuilder = new GsonBuilder();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create( System.getenv("admin_url") + "/api/v1/companies/"+ id))
                .PUT(HttpRequest.BodyPublishers.ofString(gsonBuilder.create().toJson(adminCompanyRequest)))
                .header("Content-Type", "application/json")
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void deleteCompanyOnAdminService(Long id) throws IOException, InterruptedException {
        // This method should call the administrator service to delete a company
        Logger.getLogger(CompanyService.class.getName()).log(Level.INFO, "Deleting company on admin service with id: {0}", id);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(System.getenv("admin_url") + "/api/v1/companies/" + id))
                .DELETE()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
