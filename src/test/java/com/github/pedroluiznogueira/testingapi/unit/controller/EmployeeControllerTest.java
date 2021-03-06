package com.github.pedroluiznogueira.testingapi.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pedroluiznogueira.testingapi.exception.ResourceAlreadyExistException;
import com.github.pedroluiznogueira.testingapi.exception.ResourceNotFoundException;
import com.github.pedroluiznogueira.testingapi.exception.error.Error;
import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.github.pedroluiznogueira.testingapi.support.ControllerSupport.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("create employee")
    public void givenEmployee_whenCreateEmployee_thenReturnCreatedEmployee() throws Exception {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        final Employee returnEmployee = Employee.builder()
                .id(1L)
                .firstName(employee.getFirstName())
                .secondName(employee.getSecondName())
                .email(employee.getEmail())
                .build();
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(returnEmployee);
        final String body = objectMapper.writeValueAsString(employee);

        // when
        final ResultActions response = mockMvc.perform(post(EMPLOYEES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath(FIRST_NAME, is(employee.getFirstName())))
                .andExpect(jsonPath(SECOND_NAME, is(employee.getSecondName())))
                .andExpect(jsonPath(EMAIL, is(employee.getEmail())));
    }

    @Test
    @DisplayName("create employee email already exist")
    public void givenEmployee_whenCreateEmployee_thenThrowEmployeeAlreadyExist() throws Exception {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        final String body = objectMapper.writeValueAsString(employee);
        final Error expectedError = Error.builder()
                .message("Employee with email : 'johnwillick@johnwillick.com' already exists")
                .detail("uri=/api/employees")
                .build();
        when(employeeService.createEmployee(employee)).thenThrow(new ResourceAlreadyExistException("Employee", "email", employee.getEmail()));

        // when
        final ResultActions response = mockMvc.perform(post(EMPLOYEES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE, is(expectedError.getMessage())))
                .andExpect(jsonPath(DETAIL, is(expectedError.getDetail())));
    }

    @Test
    @DisplayName("get employees")
    public void givenEmployees_whenGetEmployees_thenReturnEmployees() throws Exception {
        // given
        final Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        final List<Employee> employees = List.of(employee);
        when(employeeService.getEmployees()).thenReturn(employees);

        // when
        final ResultActions response = mockMvc.perform(get(EMPLOYEES_URI));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIZE, is(employees.size())));
    }

    @Test
    @DisplayName("get employees empty")
    public void givenNoEmployees_whenGetEmployees_thenReturnNoEmployees() throws Exception {
        // given
        final List<Employee> employees = List.of();
        when(employeeService.getEmployees()).thenReturn(employees);

        // when
        final ResultActions response = mockMvc.perform(get(EMPLOYEES_URI));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIZE, is(0)));
    }

    @Test
    @DisplayName("get employee by id")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given
        final Long id = 1L;
        final Employee returnEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        when(employeeService.getEmployeeById(id)).thenReturn(returnEmployee);

        // when
        final ResultActions response = mockMvc.perform(get(EMPLOYEES_URI + "/" + id));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is("John")))
                .andExpect(jsonPath(SECOND_NAME, is("Willick")))
                .andExpect(jsonPath(EMAIL, is("johnwillick@johnwillick.com")));
    }

    @Test
    @DisplayName("get employee by id not found")
    public void givenEmployeeId_whenGetEmployeeById_thenThrowEmployeeNotFound() throws Exception {
        // given
        final Long id = 1L;
        final Error expectedError = Error.builder()
                .message("Employee not found with id : '1'")
                .detail("uri=/api/employees/1")
                .build();
        when(employeeService.getEmployeeById(id)).thenThrow(new ResourceNotFoundException("Employee", "id", id.toString()));

        // when
        final ResultActions response = mockMvc.perform(get(EMPLOYEES_URI + "/" + id));

        // then
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE, is(expectedError.getMessage())))
                .andExpect(jsonPath(DETAIL, is(expectedError.getDetail())));
    }

    @Test
    @DisplayName("update employee")
    public void givenEmployeeData_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given
        final Employee receivedEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        final Employee returnEmployee = Employee.builder()
                .id(1L)
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        when(employeeService.updateEmployee(receivedEmployee)).thenReturn(returnEmployee);
        final String body = objectMapper.writeValueAsString(receivedEmployee);

        // when
        final ResultActions response = mockMvc.perform(put(EMPLOYEES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is(receivedEmployee.getFirstName())))
                .andExpect(jsonPath(SECOND_NAME, is(receivedEmployee.getSecondName())))
                .andExpect(jsonPath(EMAIL, is(receivedEmployee.getEmail())));
    }

    @Test
    @DisplayName("update employee not found")
    public void givenEmployeeData_whenUpdateEmployee_thenThrowEmployeeNotFound() throws Exception {
        // given
        final Long id = 1L;
        final Error expectedError = Error.builder()
                .message("Employee not found with id : '1'")
                .detail("uri=/api/employees")
                .build();
        when(employeeService.updateEmployee(any(Employee.class))).thenThrow(new ResourceNotFoundException("Employee", "id", id.toString()));
        final String body = objectMapper.writeValueAsString(Employee.builder().build());

        // when
        final ResultActions response = mockMvc.perform(put(EMPLOYEES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE, is(expectedError.getMessage())))
                .andExpect(jsonPath(DETAIL, is(expectedError.getDetail())));
    }

    @Test
    @DisplayName("delete employee")
    public void givenEmployeeId_whenDeleteEmployee_thenRemoveEmployee() throws Exception {
        // given
        final Long id = 1L;

        // when
        final ResultActions response = mockMvc.perform(delete(EMPLOYEES_URI + "/" + id));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
