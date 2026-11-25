package com.example.employee.dto;

import com.example.employee.entity.Employee;

import java.math.BigDecimal;

public class EmployeeResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final String department;
    private final BigDecimal salary;
    private final String designation;

    public EmployeeResponse(Long id, String name, String email, String department,
                            BigDecimal salary, String designation) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.designation = designation;
    }

    public static EmployeeResponse from(Employee e) {
        return new EmployeeResponse(e.getId(), e.getName(), e.getEmail(),
                e.getDepartment(), e.getSalary(), e.getDesignation());
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public BigDecimal getSalary() { return salary; }
    public String getDesignation() { return designation; }
}
