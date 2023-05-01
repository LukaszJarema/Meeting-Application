package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findBySurname(String surname);
    @Query("SELECT e FROM Employee e WHERE LOWER(e.surname) LIKE CONCAT('%', :query, '%') OR LOWER(e.name) LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeesByNameOrSurname(String query);
    Employee findByEmailAddress(String emailAddress);
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.password=:password WHERE e.id=:id")
    void updateEmployeePassword(String password, Long id);
}
