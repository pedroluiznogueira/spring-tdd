package com.github.pedroluiznogueira.testingapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pedroluiznogueira.testingapi.exception.ResourceAlreadyExistException;
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

import static com.github.pedroluiznogueira.testingapi.controller.support.ControllerSupport.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void givenEmployee_whenCreateEmployee_ThenReturnCreatedEmployee() throws Exception {
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
    public void givenEmployee_whenCreateEmployee_ThenThrowEmployeeAlreadyExist() throws Exception {
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
    public void givenEmployees_whenGetEmployees_ThenReturnEmployees() throws Exception {
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
}
