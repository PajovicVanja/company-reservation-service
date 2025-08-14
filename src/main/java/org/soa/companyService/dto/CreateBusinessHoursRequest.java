package org.soa.companyService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;

@Schema(description = "Payload to create Business Hours")
public class CreateBusinessHoursRequest {
    @NotNull @Schema(example = "1") private Integer dayNumber;
    @NotNull @Schema(example = "09:00:00") private Time timeFrom;
    @NotNull @Schema(example = "17:00:00") private Time timeTo;
    @Schema(example = "12:00:00") private Time pauseFrom;
    @Schema(example = "12:30:00") private Time pauseTo;
    @NotNull @Schema(example = "1") private Long companyId;
    @NotBlank @Schema(example = "MONDAY") private String day;

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
    public Time getTimeFrom() { return timeFrom; }
    public void setTimeFrom(Time timeFrom) { this.timeFrom = timeFrom; }
    public Time getTimeTo() { return timeTo; }
    public void setTimeTo(Time timeTo) { this.timeTo = timeTo; }
    public Time getPauseFrom() { return pauseFrom; }
    public void setPauseFrom(Time pauseFrom) { this.pauseFrom = pauseFrom; }
    public Time getPauseTo() { return pauseTo; }
    public void setPauseTo(Time pauseTo) { this.pauseTo = pauseTo; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
}
