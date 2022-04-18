package com.github.pedroluiznogueira.testingapi.client.api;

import com.github.pedroluiznogueira.testingapi.client.EmployeeClient;
import com.github.pedroluiznogueira.testingapi.client.EmployeeClientBuilder;
import com.github.pedroluiznogueira.testingapi.model.Employee;

import java.util.List;

public abstract class EmployeeApi {

    private static final EmployeeClientBuilder employeeClientBuilder = new EmployeeClientBuilder();
    private static final EmployeeClient employeeClient = employeeClientBuilder.getEmployeeClient();

    public static Employee createEmployee(final Employee employee) {
        return employeeClient.createEmployee(employee);
    }

    public static List<Employee> getEmployees() {
        return employeeClient.getEmployees();
    }

    public static Employee getEmployeeById(final Long id) {
        return employeeClient.getEmployeeById(id);
    }

    public static Employee updateEmployee(final Employee employee) {
        return employeeClient.updateEmployee(employee);
    }

    public static void deleteEmployee(final Long id) {
        employeeClient.deleteEmployee(id);
    }
}
