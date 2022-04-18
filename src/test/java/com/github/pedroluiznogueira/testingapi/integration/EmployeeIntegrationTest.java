package com.github.pedroluiznogueira.testingapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.github.pedroluiznogueira.testingapi.support.ControllerSupport.*;
import static com.github.pedroluiznogueira.testingapi.support.ControllerSupport.EMAIL;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("create employee")
    public void givenEmployee_whenCreateEmployee_thenReturnCreatedEmployee() throws Exception {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
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
        employeeRepository.saveAll(employees);

        // when
        final ResultActions response = mockMvc.perform(get(EMPLOYEES_URI));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(SIZE, is(employees.size())));
    }

    @Test
    @DisplayName("get employee by id")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        final Employee createdEmployee = employeeRepository.save(employee);

        // when
        final ResultActions response = mockMvc.perform(get(EMPLOYEES_URI + "/" + createdEmployee.getId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(FIRST_NAME, is("John")))
                .andExpect(jsonPath(SECOND_NAME, is("Willick")))
                .andExpect(jsonPath(EMAIL, is("johnwillick@johnwillick.com")));
    }

}
