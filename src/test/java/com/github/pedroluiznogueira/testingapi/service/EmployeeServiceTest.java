package com.github.pedroluiznogueira.testingapi.service;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.repository.EmployeeRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("create employee")
    public void givenEmployee_whenCreateEmployee_thenReturnPersistedEmployee() {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        final Employee returnEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        when(employeeRepository.save(any(Employee.class))).thenReturn(returnEmployee);

        // when
        final Employee persistedEmployee = employeeService.createEmployee(employee);

        // then
        assertThat(persistedEmployee).isNotNull();
        assertThat(persistedEmployee.getId()).isGreaterThan(0);
        assertThat(persistedEmployee).usingRecursiveComparison().ignoringFields("id").isEqualTo(employee);
    }

    @Test
    @DisplayName("create employe when email already exists")
    public void givenEmployee_whenCreateEmployee_thenThrowEmailAlreadyExists() {
        // given
        final Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        // when
        final Executable lambda = () -> employeeService.createEmployee(employee);

        // then
        assertThrows(IllegalArgumentException.class, lambda);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("get employees")
    public void givenEmployees_whenGetEmployees_thenReturnEmployees() {
        // given
        final Employee firstEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        final Employee secondEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        final List<Employee> employees = List.of(firstEmployee, secondEmployee);

        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        final List<Employee> retrievedEmployees = employeeService.getEmployees();

        // then
        assertThat(retrievedEmployees).isNotNull();
        assertThat(retrievedEmployees.size()).isEqualTo(employees.size());
    }

}
