package com.github.pedroluiznogueira.testingapi.service;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("createEmployee()")
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
}
