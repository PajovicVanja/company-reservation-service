package org.soa.reservation_service.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.soa.reservation_service.client.EmployeeClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/_probe/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeProbeController {

    private final EmployeeClient employeeClient;

    @Autowired
    public EmployeeProbeController(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    // Simple passthrough of GET /employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEmployee(@PathVariable Long id) {
        Map<String, Object> body = employeeClient.getEmployeeRaw(id);
        return ResponseEntity.ok(body);
    }

    // Optional: skills + availability passthroughs
    @GetMapping("/{id}/skills")
    public ResponseEntity<List<Map<String, Object>>> getSkills(@PathVariable Long id) {
        return ResponseEntity.ok(employeeClient.getSkillsRaw(id));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<List<Map<String, Object>>> getAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(employeeClient.getAvailabilityRaw(id));
    }

    // Optional: “assert-active” that just returns 204 if OK, 404 if not
    @GetMapping("/{id}/assert-active")
    public ResponseEntity<Void> assertActive(@PathVariable Long id) {
        employeeClient.assertEmployeeActive(id); // throws if not found/inactive
        return ResponseEntity.noContent().build();
    }
}
