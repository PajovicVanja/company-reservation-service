package org.soa.reservation_service.dto;

import java.time.LocalDateTime;

public class FreeTerminRequest {
    public LocalDateTime date;
    public Long serviceId;
    public Long employeeId;
}
