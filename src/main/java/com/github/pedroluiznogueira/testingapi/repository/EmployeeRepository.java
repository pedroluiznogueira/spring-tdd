package com.github.pedroluiznogueira.testingapi.repository;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(final String email);
}
