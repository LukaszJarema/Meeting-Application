package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAllEmployees();
    Employee saveEmployee(EmployeeDto employeeDto);
    EmployeeDto findEmployeeById(long employeeId);
    void updateEmployee(EmployeeDto employee);
    void delete(Long employeeId);
    List<EmployeeDto> searchEmployeesByNameOrSurname(String query);
    Employee findByEmail(String emailAddress);
}
