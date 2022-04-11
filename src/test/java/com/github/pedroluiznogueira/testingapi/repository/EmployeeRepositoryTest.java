package com.github.pedroluiznogueira.testingapi.repository;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired private EmployeeRepository employeeRepository;

    @Test
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

}
