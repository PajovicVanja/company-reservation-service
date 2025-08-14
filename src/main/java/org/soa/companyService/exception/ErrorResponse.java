package org.soa.companyService.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Standard error payload returned on errors")
public class ErrorResponse {
    @Schema(example = "2025-08-14T08:30:45.123+02:00")
    private OffsetDateTime timestamp;
    @Schema(example = "404")
    private int status;
    @Schema(example = "Not Found")
    private String error;
    @Schema(example = "Company not found with id 123")
    private String message;
    @Schema(example = "/api/companies/123")
    private String path;

    public ErrorResponse() {}

    public ErrorResponse(OffsetDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp; this.status = status; this.error = error; this.message = message; this.path = path;
    }

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
