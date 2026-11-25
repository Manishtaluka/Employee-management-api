package com.example.employee.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "employees",
        uniqueConstraints = @UniqueConstraint(name = "uk_employee_email", columnNames = "email"),
        indexes = @Index(name = "idx_employee_department", columnList = "department"))
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false, length = 100)
    private String designation;

    public Employee() {
    }

    public Employee(String name, String email, String department, BigDecimal salary, String designation) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.designation = designation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}
