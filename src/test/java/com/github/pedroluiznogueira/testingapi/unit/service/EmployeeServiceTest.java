package com.github.pedroluiznogueira.testingapi.unit.service;

import com.github.pedroluiznogueira.testingapi.exception.ResourceAlreadyExistException;
import com.github.pedroluiznogueira.testingapi.exception.ResourceNotFoundException;
import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.repository.EmployeeRepository;
import com.github.pedroluiznogueira.testingapi.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("create employe email already exists")
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
        final Executable executable = () -> employeeService.createEmployee(employee);

        // then
        assertThrows(ResourceAlreadyExistException.class, executable);
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

    @Test
    @DisplayName("get employees empty")
    public void givenEmptyEmployees_whenGetEmployees_thenReturnEmptyEmployees() {
        // given
        final List<Employee> employees = List.of();
        when(employeeRepository.findAll()).thenReturn(employees);

        // when
        final List<Employee> retrievedEmployees = employeeService.getEmployees();

        // then
        assertThat(retrievedEmployees).isNotNull();
        assertThat(retrievedEmployees.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("find employe by id")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        // given
        final Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        // when
        final Employee foundEmployee = employeeService.getEmployeeById(employee.getId());

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getId()).isEqualTo(employee.getId());
        assertThat(foundEmployee).usingRecursiveComparison().ignoringFields("id").isEqualTo(employee);
    }

    @Test
    @DisplayName("find employe by id not found")
    public void givenEmployeeId_whenGetEmployeeById_thenThrowEmployeeNotFound() {
        // given
        final Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        // when
        final Executable executable = () -> employeeService.getEmployeeById(employee.getId());

        // then
        assertThrows(ResourceNotFoundException.class, executable);
        verify(employeeRepository, times(1)).findById(employee.getId());
    }

    @Test
    @DisplayName("update employee")
    public void givenEmployeData_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        final Employee employeeData = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        final Employee employeeToUpdate = spy(Employee.builder()
                .id(1L)
                .firstName("Jocko")
                .secondName("Willick")
                .email("jockowillick@jockowillick.com")
                .build());
        final Employee employeeDataToUpdate = Employee.builder()
                .id(employeeToUpdate.getId())
                .firstName(employeeData.getFirstName())
                .secondName(employeeData.getSecondName())
                .email(employeeData.getEmail())
                .build();
        when(employeeRepository.findById(employeeData.getId())).thenReturn(Optional.of(employeeToUpdate));
        when(employeeRepository.save(employeeDataToUpdate)).thenReturn(employeeDataToUpdate);

        // when
        final Employee updatedEmployee = employeeService.updateEmployee(employeeDataToUpdate);

        // then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employeeToUpdate.getId());
        assertThat(updatedEmployee).usingRecursiveComparison().ignoringFields("id").isNotEqualTo(employeeToUpdate);
        verify(employeeToUpdate, times(3)).getId();
    }

    @Test
    @DisplayName("update employee not found")
    public void givenEmployeData_whenUpdateEmployee_thenThrowEmployeeNotFound() {
        // given
        final Employee employeeData = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        when(employeeRepository.findById(employeeData.getId())).thenReturn(Optional.empty());

        // when
        final Executable executable = () -> employeeService.updateEmployee(employeeData);

        // then
        assertThrows(ResourceNotFoundException.class, executable);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("delete employee")
    public void givenEmployeId_whenDeleteEmployee_thenRemoveEmployee() {
        // given
        final Long id = 1L;
        final Employee returnEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Wick")
                .email("johnwick@johnwick.com")
                .build();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(returnEmployee));

        // when
        employeeService.deleteEmployee(id);

        // then
        verify(employeeRepository, times(1)).delete(any(Employee.class));
        verify(employeeRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("delete employee")
    public void givenEmployeId_whenDeleteEmployee_thenThrowEmployeeNotFound() {
        // given
        final Long id = 1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // when
        final Executable executable = () -> employeeService.deleteEmployee(id);

        // then
        assertThrows(ResourceNotFoundException.class, executable);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

}
