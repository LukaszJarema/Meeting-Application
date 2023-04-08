package com.jarema.lukasz.Meeting.Application.dtos;

import com.jarema.lukasz.Meeting.Application.models.Role;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDto {
    private Long id;
    private String name;
    private String surname;
    private String emailAddress;
    private String department;
    private String password;
    private String telephoneNumber;
    private Role role;
}
