package com.github.pedroluiznogueira.testingapi.repository;

import com.github.pedroluiznogueira.testingapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(final String email);

    @Query("select e from Employee e where e.firstName = ?1 and e.secondName = ?2")
    Optional<Employee> findByJPQL(final String firstName, final String secondName);

    @Query("select e from Employee e where e.firstName =:firstName and e.secondName =:secondName")
    Optional<Employee> findByJPQLNamedParams(final String firstName, final String secondName);

    @Query(value = "select * from employee e where e.first_name =?1 and e.second_name =?2", nativeQuery = true)
    Optional<Employee> findByNativeQuery(final String firstName, final String secondName);

    @Query(value = "select * from employee e where e.first_name =:firstName and e.second_name =:secondName", nativeQuery = true)
    Optional<Employee> findByNativeQueryNamedParams(final String firstName, final String secondName);
}
