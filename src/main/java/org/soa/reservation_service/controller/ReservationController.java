package org.soa.reservation_service.controller;

import org.soa.reservation_service.dto.*;
import org.soa.reservation_service.model.Reservation;
import org.soa.reservation_service.service.ReservationService;
import org.soa.reservation_service.service.StatusService;
import org.soa.reservation_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/company/{idCompany}")
    public List<Reservation> getAllReservationsByCompany(@PathVariable Long idCompany) {
        return reservationService.getAllReservationsByCompany(idCompany);
    }

    @GetMapping("/user/{idUser}")
    public List<Reservation> getAllReservationsByUser(@PathVariable Long idUser) {
        return reservationService.getAllReservationsByUser(idUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationCreateRequest request) {
        Reservation createdReservation = reservationService.createReservation(request);
        return ResponseEntity.ok(createdReservation);
    }

    /**
     * Changed from GET-with-body to GET-with-query-params.
     * Example:
     *   GET /api/reservations/free-slots/42?date=2025-01-31T10:00:00&serviceId=7&employeeId=123
     * `employeeId` is optional.
     */
    @GetMapping("/free-slots/{idCompany}")
    public ResponseEntity<List<String>> getFreeSlots(
            @PathVariable Long idCompany,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam("serviceId") Long serviceId,
            @RequestParam(value = "employeeId", required = false) Long employeeId) {

        List<String> list = reservationService.getFreeSlots(
                idCompany,
                date,
                serviceId,
                employeeId
        );
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/confirm/{twoFACode}")
    public ResponseEntity<Reservation> confirmReservation(@PathVariable Long id, @PathVariable Long twoFACode) {
        try {
            Reservation updatedReservation = reservationService.confirmReservation(id, twoFACode);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(@PathVariable Long id, @RequestBody ReservationStatusUpdateRequest request) {
        try {
            Reservation updatedReservation = reservationService.updateReservationStatus(id, request.getStatusId());
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Reservation> adminUpdateReservation(@PathVariable Long id, @RequestBody AdminReservationUpdateRequest request) {
        try {
            Reservation updatedReservation = reservationService.adminUpdateReservation(id, request.getStatusId());
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
