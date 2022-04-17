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

    public Employee getEmployeeById(final Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("unable to find employee with the given id"));
    }

    public Employee updateEmployee(final Employee employeeData) {
        final Employee employeeToUpdate = employeeRepository.findById(employeeData.getId()).orElseThrow(() -> new IllegalArgumentException("unable to find employee with the given id"));
        final Employee employeeDataToUpdate = Employee.builder()
                .id(employeeToUpdate.getId())
                .firstName(employeeData.getFirstName())
                .secondName(employeeData.getSecondName())
                .email(employeeData.getEmail())
                .build();
        return employeeRepository.save(employeeDataToUpdate);
    }

    public void deleteEmployee(final Long id) {
        final Employee employeeToDelete = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("unable to find employee with the given id"));
        employeeRepository.delete(employeeToDelete);
    }
}
