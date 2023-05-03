package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
    public Employee findByEmail(String emailAddress) {
        return employeeRepository.findByEmailAddress(emailAddress);
    }

    private Employee mapToEmployee(EmployeeDto employee) {
        Employee employeeDto = Employee.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .emailAddress(employee.getEmailAddress())
                .department(employee.getDepartment())
                .telephoneNumber(employee.getTelephoneNumber())
                .password(passwordEncoder.encode(employee.getPassword()))
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
                .password(passwordEncoder.encode(employee.getPassword()))
                .role((Role) Collections.singleton(employee.getRole()))
                .build();
        return employeeDto;
    }
}
