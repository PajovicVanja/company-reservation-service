package org.soa.reservation_service.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Standard error payload returned on errors")
public class ErrorResponse {
    @Schema(example = "2025-08-14T08:30:45.123+02:00") private OffsetDateTime timestamp;
    @Schema(example = "404") private int status;
    @Schema(example = "Not Found") private String error;
    @Schema(example = "Entity not found") private String message;
    @Schema(example = "/api/resource/1") private String path;

    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
