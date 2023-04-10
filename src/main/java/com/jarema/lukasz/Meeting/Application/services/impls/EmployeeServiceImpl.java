package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;
    private EmployeeDto employeeDto;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((employee) -> mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    @Override
    public Employee saveEmployee(EmployeeDto employeeDto) {
        Employee employee = mapToEmployee(employeeDto);
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeDto findEmployeeById(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).get();
        return mapToEmployeeDto(employee);
    }

    @Override
    public void updateEmployee(EmployeeDto employeeDto) {
        Employee employee = mapToEmployee(employeeDto);
        employeeRepository.save(employee);
    }

    @Override
    public void delete(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public List<EmployeeDto> searchEmployeesByNameOrSurname(String query) {
        List<Employee> employees = employeeRepository.searchEmployeesByNameOrSurname(query.toLowerCase());
        return employees.stream().map(employee -> mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    @Override
    public void updateEmployeePassword(@Param("password") String password, @Param("id") Long employeeId) {
        Employee employee = mapToEmployee(employeeDto);
        employeeDto.setPassword(password);
        employeeRepository.save(employee);
    }

    private Employee mapToEmployee(EmployeeDto employee) {
        Employee employeeDto = Employee.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .emailAddress(employee.getEmailAddress())
                .department(employee.getDepartment())
                .telephoneNumber(employee.getTelephoneNumber())
                .password(employee.getPassword())
                .role(Collections.singleton(employee.getRole()))
                .build();
        return employeeDto;
    }

    private EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .emailAddress(employee.getEmailAddress())
                .department(employee.getDepartment())
                .telephoneNumber(employee.getTelephoneNumber())
                .password(employee.getPassword())
                .build();
        return employeeDto;
    }
}
