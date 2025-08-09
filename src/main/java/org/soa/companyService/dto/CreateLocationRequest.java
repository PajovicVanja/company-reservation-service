package org.soa.companyService.dto;

public class CreateLocationRequest {
    private String name;
    private String number;
    private Long parentLocationId; // Reference by ID instead of the full Location object

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public void setParentLocationId(Long parentLocationId) {
        this.parentLocationId = parentLocationId;
    }
}
