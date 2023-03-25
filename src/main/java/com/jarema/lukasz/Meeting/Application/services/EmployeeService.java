package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAllEmployees();
}
