package com.jarema.lukasz.Meeting.Application.repository;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
