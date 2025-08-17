package org.soa.companyService.service;

import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soa.companyService.exception.NotFoundException;
import org.soa.companyService.model.ServiceM;
import org.soa.companyService.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    Logger logger = LoggerFactory.getLogger(ServiceService.class);

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private S3ClientService s3ClientService;

    public List<ServiceM> getAllServices(Long companyId) {
        return serviceRepository.findByCompanyId(companyId);
    }

    public List<ServiceM> getAllServices(Long companyId, Long category) {
        return serviceRepository.findByCompanyIdAndCategory(companyId, category);
    }

    public Optional<ServiceM> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public ServiceM createService(ServiceM service) {
        return serviceRepository.save(service);
    }

    public ServiceM createService(ServiceM service, MultipartFile file) {
        ServiceM newService = serviceRepository.save(service);
        return uploadPicture(newService.getId(), file);
    }

    public ServiceM updateService(Long id, ServiceM updatedService) {
        return serviceRepository.findById(id)
                .map(existingService -> {
                    existingService.setCategory(updatedService.getCategory());
                    existingService.setCompany(updatedService.getCompany());
                    existingService.setName(updatedService.getName());
                    existingService.setDescription(updatedService.getDescription());
                    existingService.setPrice(updatedService.getPrice());
                    existingService.setIdPicture(updatedService.getIdPicture());
                    existingService.setDuration(updatedService.getDuration());
                    return serviceRepository.save(existingService);
                })
                .orElseThrow(() -> new NotFoundException("Service not found with id " + id));
    }

    public void deleteService(Long id) {
        serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Service not found with id " + id));
        serviceRepository.deleteById(id);
    }

    public ServiceM uploadPicture(Long id, MultipartFile file) {
        return serviceRepository.findById(id)
                .map(existingService -> {
                    try {
                        String path = s3ClientService.uploadPicture(
                                file,
                                id,
                                existingService.getCompany().getId()
                        );
                        existingService.setIdPicture(path);
                        return serviceRepository.save(existingService);
                    } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                             InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException |
                             ServerException | XmlParserException e) {
                        logger.error("Error uploading file: " + e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new NotFoundException("Service not found with id " + id));
    }
}
