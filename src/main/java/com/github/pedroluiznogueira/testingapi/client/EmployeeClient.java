package com.github.pedroluiznogueira.testingapi.client;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import feign.RequestLine;

import java.util.List;

public interface EmployeeClient {
    @RequestLine("GET")
    List<Employee> getEmployees();
}
