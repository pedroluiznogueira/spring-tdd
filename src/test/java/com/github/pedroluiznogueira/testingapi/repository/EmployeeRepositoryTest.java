package com.github.pedroluiznogueira.testingapi.repository;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("save()")
    public void givenEmployeeObject_whenSave_thenReturnPersistedEmployee() {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();

        // when
        final Employee persistedEmployee = employeeRepository.save(employee);

        // then
        assertThat(persistedEmployee).usingRecursiveComparison().isNotNull();
        assertThat(persistedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("findAll()")
    public void givenEmployeesList_whenFindAll_thenReturnPersistedEmployees() {
        // given
        final List<Employee> employees = List.of(
                Employee.builder()
                        .firstName("John")
                        .secondName("Wick")
                        .email("johnwick@johnwick.com")
                        .build(),
                Employee.builder()
                        .firstName("Jocko")
                        .secondName("Willick")
                        .email("jockowillick@jockowillick.com")
                        .build()
        );
        employeeRepository.saveAll(employees);

        // when
        final List<Employee> persistedEmployees = employeeRepository.findAll();

        // then
        assertThat(persistedEmployees).usingRecursiveComparison().isNotNull();
        assertThat(persistedEmployees.size()).usingRecursiveComparison().isEqualTo(2);
    }

    @Test
    @DisplayName("findById()")
    public void givenEmployeeId_whenFindById_thenReturnPersistedEmployeeById() {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        final Employee persistedEmployee = employeeRepository.save(employee);
        final Long persistedEmployeeId = persistedEmployee.getId();

        // when
        final Employee foundEmployee = employeeRepository.findById(persistedEmployeeId).orElseThrow(() -> new IllegalArgumentException("unable to find employee with the given id"));

        // then
        assertThat(foundEmployee).usingRecursiveComparison().isNotNull();
        assertThat(foundEmployee.getId()).usingRecursiveComparison().isEqualTo(persistedEmployee.getId());
    }

    @Test
    @DisplayName("findByEmail()")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnPersistedEmployeeByEmail() {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        final Employee persistedEmployee = employeeRepository.save(employee);
        final String persistedEmployeeEmail = persistedEmployee.getEmail();

        // when
        final Employee foundEmployee = employeeRepository.findByEmail(persistedEmployeeEmail).orElseThrow(() -> new IllegalArgumentException("unable to find employee with the given email"));

        // then
        assertThat(foundEmployee).usingRecursiveComparison().isNotNull();
        assertThat(foundEmployee.getEmail()).isEqualTo(persistedEmployeeEmail);
    }

}
