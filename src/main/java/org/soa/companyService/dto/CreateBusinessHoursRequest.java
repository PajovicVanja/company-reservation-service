package org.soa.companyService.dto;

import java.sql.Time;

public class CreateBusinessHoursRequest {
    private Integer dayNumber;
    private Time timeFrom;
    private Time timeTo;
    private Time pauseFrom;
    private Time pauseTo;
    private Long companyId; // Reference by ID instead of the full Company object
    private String day;

    // Getters and Setters

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Time getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Time timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Time getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Time timeTo) {
        this.timeTo = timeTo;
    }

    public Time getPauseFrom() {
        return pauseFrom;
    }

    public void setPauseFrom(Time pauseFrom) {
        this.pauseFrom = pauseFrom;
    }

    public Time getPauseTo() {
        return pauseTo;
    }

    public void setPauseTo(Time pauseTo) {
        this.pauseTo = pauseTo;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
