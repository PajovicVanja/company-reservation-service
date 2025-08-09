package org.soa.reservation_service.dto;

import java.time.*;

public class TimeInterval {
    public LocalTime start;
    public LocalTime end;

    public TimeInterval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(TimeInterval other) {
        return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }
}
