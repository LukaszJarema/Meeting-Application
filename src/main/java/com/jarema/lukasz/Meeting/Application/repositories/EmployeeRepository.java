package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findBySurname(String surname);
    @Query("SELECT e FROM Employee e WHERE LOWER(e.surname) LIKE CONCAT('%', :query, '%') OR LOWER(e.name) LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeesByNameOrSurname(String query);
    Employee findByEmailAddress(String emailAddress);
    Employee findByName(String name);
}
