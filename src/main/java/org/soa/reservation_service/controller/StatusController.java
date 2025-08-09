package org.soa.reservation_service.controller;

import org.soa.reservation_service.dto.StatusCreateRequest;
import org.soa.reservation_service.dto.StatusUpdateRequest;
import org.soa.reservation_service.model.Status;
import org.soa.reservation_service.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping
    public List<Status> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable Long id) {
        return statusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Status> createStatus(@RequestBody StatusCreateRequest request) {
        Status status = new Status();
        status.setName(request.getName());
        status.setAddedBy(request.getAddedBy());

        Status createdStatus = statusService.createStatus(status);
        return ResponseEntity.ok(createdStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Status status = new Status();
            status.setName(request.getName());
            status.setAddedBy(request.getAddedBy());

            Status updatedStatus = statusService.updateStatus(id, status);
            return ResponseEntity.ok(updatedStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
