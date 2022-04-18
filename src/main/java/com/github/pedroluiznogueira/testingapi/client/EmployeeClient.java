package com.github.pedroluiznogueira.testingapi.client;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface EmployeeClient {
    @RequestLine("GET")
    List<Employee> getEmployees();

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    Employee createEmployee(final Employee employee);

    @RequestLine("GET /{id}")
    Employee getEmployeeById(@Param("id") final Long id);

    @RequestLine("PUT")
    @Headers("Content-Type: application/json")
    Employee updateEmployee(Employee employee);

    @RequestLine("DELETE /{id}")
    void deleteEmployee(@Param("id") final Long id);
}
