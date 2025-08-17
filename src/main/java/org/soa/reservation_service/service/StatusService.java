package org.soa.reservation_service.service;

import org.soa.reservation_service.exception.NotFoundException;
import org.soa.reservation_service.model.Status;
import org.soa.reservation_service.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public Optional<Status> findStatusByName(String name) {
        return statusRepository.findByName(name);
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Optional<Status> getStatusById(Long id) {
        return statusRepository.findById(id);
    }

    public Status getDefaultStatus() {
        return statusRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Default status not found"));
    }

    public Status getConfirmStatus() {
        return statusRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Confirm status not found"));
    }

    public Status createStatus(Status status) {
        return statusRepository.save(status);
    }

    public Status updateStatus(Long id, Status updatedStatus) {
        return statusRepository.findById(id)
                .map(existingStatus -> {
                    existingStatus.setName(updatedStatus.getName());
                    existingStatus.setAddedBy(updatedStatus.getAddedBy());
                    return statusRepository.save(existingStatus);
                })
                .orElseThrow(() -> new NotFoundException("Status not found with id " + id));
    }

    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
}
