package com.github.pedroluiznogueira.testingapi.support;

import com.github.pedroluiznogueira.testingapi.model.Employee;

import java.util.List;

import static com.github.pedroluiznogueira.testingapi.support.StringSupport.generate;

public class ListSupport {

    public static List<Employee> generateEmployees() {
        final Employee firstEmployee = Employee.builder()
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();
        final Employee secondEmployee = Employee.builder()
                .firstName(generate())
                .secondName(generate())
                .email(generate())
                .build();
        return List.of(firstEmployee, secondEmployee);
    }
}
