package com.github.pedroluiznogueira.testingapi.service;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(final Employee employee) {
        employeeRepository.findByEmail(employee.getEmail()).ifPresent(foundEmployee -> {
            throw new IllegalArgumentException("unable to create employee, the given email already exists");
        });
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }
}
