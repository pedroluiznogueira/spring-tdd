package com.github.pedroluiznogueira.testingapi.unit.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pedroluiznogueira.testingapi.exception.ResourceAlreadyExistException;
import com.github.pedroluiznogueira.testingapi.exception.error.Error;
import com.github.pedroluiznogueira.testingapi.model.Employee;
import com.github.pedroluiznogueira.testingapi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.pedroluiznogueira.testingapi.support.ControllerSupport.EMPLOYEES_URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest
public class ErrorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("global handler")
    public void givenAnyErrorScenario_whenThrowException_thenReturnGlobalError() throws Exception {
        // given
        final Employee employee = Employee.builder()
                .firstName("John")
                .secondName("Willick")
                .email("johnwillick@johnwillick.com")
                .build();
        when(employeeService.createEmployee(employee)).thenThrow(new ResourceAlreadyExistException("Employee", "email", employee.getEmail()));
        final String body = objectMapper.writeValueAsString(employee);

        // when
        final ResultActions response = mockMvc.perform(post(EMPLOYEES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        final String json = response.andReturn().getResponse().getContentAsString();
        final Error error = objectMapper.readValue(json, Error.class);

        // then
        response.andDo(print()).andExpect(status().isBadRequest());
        assertThat(error).isNotNull();
    }
}
