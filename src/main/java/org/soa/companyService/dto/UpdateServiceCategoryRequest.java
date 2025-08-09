package org.soa.companyService.dto;

public class UpdateServiceCategoryRequest {
    private String name;
    private Long companyId; // Reference by ID instead of the full Company object

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
