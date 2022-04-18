package com.github.pedroluiznogueira.testingapi.service;

import com.github.pedroluiznogueira.testingapi.exception.ResourceAlreadyExistException;
import com.github.pedroluiznogueira.testingapi.exception.ResourceNotFoundException;
import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(final Employee employee) {
        checkIfEmailExists(employee);
        return employeeRepository.save(employee);
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(final Long id) {
        return getEmployee(id);
    }

    public Employee updateEmployee(final Employee employeeData) {
        final Employee employeeToUpdate = getEmployee(employeeData.getId());
        final Employee employeeDataToUpdate = Employee.builder()
                .id(employeeToUpdate.getId())
                .firstName(employeeData.getFirstName())
                .secondName(employeeData.getSecondName())
                .email(employeeData.getEmail())
                .build();
        return employeeRepository.save(employeeDataToUpdate);
    }

    public void deleteEmployee(final Long id) {
        final Employee employeeToDelete = getEmployee(id);
        employeeRepository.delete(employeeToDelete);
    }

    private Employee getEmployee(final Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id.toString()));
    }

    private void checkIfEmailExists(final Employee employee) {
        employeeRepository.findByEmail(employee.getEmail()).ifPresent((foundEmployee) -> {
            throw new ResourceAlreadyExistException("Employee", "email", employee.getEmail());
        });
    }
}
