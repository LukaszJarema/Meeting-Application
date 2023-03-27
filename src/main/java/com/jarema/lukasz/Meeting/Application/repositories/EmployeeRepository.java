package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findBySurname(String surname);
    @Query("SELECT e FROM Employee e WHERE e.surname LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeesBySurname(String query);

}
