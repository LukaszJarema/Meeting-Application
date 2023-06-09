package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private EmailService emailService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder, EmailService emailService) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((employee) -> mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    @Override
    public Employee saveEmployee(EmployeeDto employeeDto) {
        Employee employee = mapToEmployee(employeeDto);
        String password = employee.getPassword();
        String emailAddress = employee.getEmailAddress();
        emailService.sendWelcomeMessageForNewEmployee(emailAddress, password);
        employee.setPassword(passwordEncoder.encode(password));
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
        employee.setPassword(employeeRepository.findById(employee.getId()).get().getPassword());
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

    @Override
    public Long getEmployeeIdByLoggedInInformation() {
        String nameOfEmployee;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            nameOfEmployee = ((UserDetails)principal).getUsername();
        } else {
            nameOfEmployee = principal.toString();
        }
        Long id = employeeRepository.findByEmailAddress(nameOfEmployee).getId();
        return id;
    }

    @Override
    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
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
                .accountNonLocked("true")
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
                .accountNonLocked("true")
                .role(employee.getRole().stream().map(Role.class::cast).findFirst().orElse(null))
                .build();
        return employeeDto;
    }
}
