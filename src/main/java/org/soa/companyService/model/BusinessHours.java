package org.soa.companyService.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "business_hours")
public class BusinessHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "time_from", nullable = false)
    private Time timeFrom;

    @Column(name = "time_to", nullable = false)
    private Time timeTo;

    @Column(name = "pause_from")
    private Time pauseFrom;

    @Column(name = "pause_to")
    private Time pauseTo;

    @ManyToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id")
    private Company company;

    @Column(name = "day", nullable = false)
    private String day;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Time getTimeFrom() { return timeFrom; }
    public void setTimeFrom(Time timeFrom) { this.timeFrom = timeFrom; }
    public Time getTimeTo() { return timeTo; }
    public void setTimeTo(Time timeTo) { this.timeTo = timeTo; }
    public Time getPauseFrom() { return pauseFrom; }
    public void setPauseFrom(Time pauseFrom) { this.pauseFrom = pauseFrom; }
    public Time getPauseTo() { return pauseTo; }
    public void setPauseTo(Time pauseTo) { this.pauseTo = pauseTo; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
}
