package com.github.pedroluiznogueira.testingapi.functional;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.client.api.EmployeeApi;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static com.github.pedroluiznogueira.testingapi.support.ListSupport.generateEmployees;
import static com.github.pedroluiznogueira.testingapi.support.StringSupport.generate;
import static com.github.pedroluiznogueira.testingapi.client.api.EmployeeApi.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeFunctionalTest {

    @Test
    public void givenCreateEmployeeRequest_whenCreateEmployee_thenReturnCreatedEmployee() {
        // given
        final Employee employee = Employee.builder()
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();

        // when
        final Employee createdEmployee = createEmployee(employee);

        // then
        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getId()).isGreaterThan(0);
    }

    @Test
    public void givenGetEmployeesRequest_whenGetEmployees_thenReturnEmployees() {
        // given
        final List<Employee> employees = generateEmployees();

        // when
        employees.forEach(EmployeeApi::createEmployee);
        final List<Employee> foundEmployees = getEmployees();

        // then
        assertThat(foundEmployees).isNotNull();
        assertThat(foundEmployees.size()).isGreaterThanOrEqualTo(employees.size());
    }

    @Test
    public void givenGetEmployeeByIdRequest_whenGetEmployeeById_thenReturnEmployee() {
        // given
        final Employee employee = Employee.builder()
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();

        // when
        final Employee createdEmployee = createEmployee(employee);
        final Employee foundEmployee = getEmployeeById(createdEmployee.getId());

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getId()).isEqualTo(createdEmployee.getId());
    }

    @Test
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        final Employee employee = Employee.builder()
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();
        final Employee createdEmployee = createEmployee(employee);
        final Employee employeeToUpdate = Employee.builder()
                .id(createdEmployee.getId())
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();

        // when
        final Employee updatedEmployee = updateEmployee(employeeToUpdate);

        // then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(createdEmployee.getId());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenRemoveEmployee() {
        // given
        final Employee employee = Employee.builder()
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();
        final Employee createdEmployee = createEmployee(employee);

        // when
        deleteEmployee(createdEmployee.getId());
        final Executable executable = () -> getEmployeeById(createdEmployee.getId());

        // then
        assertThrows(FeignException.BadRequest.class, executable);
    }
}
