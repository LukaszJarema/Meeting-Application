package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> findAllEmployees();
}
