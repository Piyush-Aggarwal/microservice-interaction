package com.example.app1.employee;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thin transactional wrapper over {@link EmployeeRepository} so controllers (or
 * downstream callers) don't touch the persistence layer directly.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class EmployeeNotFoundException extends RuntimeException {
        EmployeeNotFoundException(Long id) {
            super("Employee not found: " + id);
        }
    }
}
