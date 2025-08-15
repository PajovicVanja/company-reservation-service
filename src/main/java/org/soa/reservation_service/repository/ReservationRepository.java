package org.soa.reservation_service.repository;

import org.aspectj.lang.annotation.AfterThrowing;
import org.soa.reservation_service.dto.Revenue;
import org.soa.reservation_service.model.Reservation;
import org.soa.reservation_service.service.interfaces.IDashboardService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.company.id = ?1 and r.date between ?2 and ?3")
    List<Reservation> findByIdCompany(Long companyId, LocalDateTime startDate, LocalDateTime endDate) throws IllegalArgumentException;

    @Query("SELECT r FROM Reservation r WHERE r.idCustomer = ?1")
    List<Reservation> findByUserId(Long userId) throws IllegalArgumentException;

    @Query("SELECT r FROM Reservation r WHERE r.company.id = ?1")
    List<Reservation> findByIdCompany(Long companyId) throws IllegalArgumentException;

    @Query("SELECT r FROM Reservation r WHERE r.notified = ?1 and r.date between ?2 and ?3")
    List<Reservation> findByDateBetween(boolean notified, LocalDateTime startDate, LocalDateTime endDate) throws IllegalArgumentException;

    @Query("SELECT r.service as name, COUNT(r) as count FROM Reservation r WHERE r.company.id = ?1 group by r.service.id")
    List<Objects> incomeByService(long companyId);

    @Query("SELECT r.service.category as name, COUNT(r) as count  FROM Reservation r WHERE r.company.id = ?1 group by r.service.category.id")
    List<Objects> incomeByServiceCategory(long companyId);

    @Query("SELECT  r.status as name, COUNT(r) as count FROM Reservation r WHERE r.company.id = ?1 group by r.status.id")
    List<Objects> statusStatistic(long companyId);

    @Query("SELECT DATE(r.date), COUNT(r) as count FROM Reservation r WHERE r.company.id = ?1 group by DATE(r.date)")
    List<Objects> dailyRevenue(long companyId);
//    @Query("SELECT r FROM Reservation r WHERE r.company.id = ?1 group by week(r.date)")
//    List<Revenue> weeklyRevenue(long companyId);
//
    @Query("SELECT month(r.date), COUNT(r) FROM Reservation r WHERE r.company.id = ?1 group by month(r.date)")
    List<Objects> monthlyRevenue(long companyId);

    List<Reservation> findByEmployeeId(Long employeeId);



}