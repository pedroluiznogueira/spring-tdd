package com.github.pedroluiznogueira.testingapi.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pedroluiznogueira.testingapi.client.EmployeeClient;
import com.github.pedroluiznogueira.testingapi.client.EmployeeClientBuilder;
import com.github.pedroluiznogueira.testingapi.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmployeeFunctionalTest {

    private ObjectMapper objectMapper;
    private EmployeeClient employeeClient;

    @BeforeEach
    public void setup() {
        EmployeeClientBuilder employeeClientBuilder = new EmployeeClientBuilder();
        employeeClient = employeeClientBuilder.getEmployeeClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void httpClient() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/employees"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        final List<Employee> employees = objectMapper.readerForListOf(Employee.class).readValue(response.body());
        assertThat(employees.size()).isGreaterThan(0);
    }

    @Test
    public void feignClient() {
        List<Employee> eemployees = employeeClient.getEmployees();
        assertThat(eemployees).isNotNull();
    }
}
