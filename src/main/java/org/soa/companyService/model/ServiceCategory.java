package org.soa.companyService.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "service_categories")
public class ServiceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_company", referencedColumnName = "id")
    private Company company;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}
