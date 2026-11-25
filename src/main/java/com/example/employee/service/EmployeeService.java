package com.example.employee.service;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateEmailException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public EmployeeResponse create(EmployeeRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException(email);
        }
        Employee employee = new Employee();
        apply(employee, request, email);
        return EmployeeResponse.from(repository.save(employee));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAll() {
        return repository.findAll().stream().map(EmployeeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        return EmployeeResponse.from(findOrThrow(id));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = findOrThrow(id);
        String email = normalizeEmail(request.getEmail());
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateEmailException(email);
        }
        apply(employee, request, email);
        return EmployeeResponse.from(repository.saveAndFlush(employee));
    }

    public void delete(Long id) {
        repository.delete(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> searchByDepartment(String department) {
        return repository.findByDepartmentIgnoreCase(department.trim())
                .stream().map(EmployeeResponse::from).toList();
    }

    // ---- helpers ----

    private Employee findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    private void apply(Employee employee, EmployeeRequest r, String normalizedEmail) {
        employee.setName(r.getName().trim());
        employee.setEmail(normalizedEmail);
        employee.setDepartment(r.getDepartment().trim());
        employee.setSalary(r.getSalary());
        employee.setDesignation(r.getDesignation().trim());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
