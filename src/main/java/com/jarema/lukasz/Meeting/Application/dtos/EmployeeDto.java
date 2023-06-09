package com.jarema.lukasz.Meeting.Application.dtos;

import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Role;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class EmployeeDto {
    private Long id;
    @NotEmpty(message = "Name could not be empty")
    private String name;
    @NotEmpty(message = "Surname could not be empty")
    private String surname;
    @NotEmpty(message = "Email address could not be empty")
    @Email(message = "Email is not valid")
    private String emailAddress;
    @NotEmpty(message = "Department could not be empty")
    private String department;
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
    @Pattern(regexp = "^[0-9]{9}$", message = "Telephone number should be contains 9 digits")
    private String telephoneNumber;
    @NotNull(message = "Choose one role for an employee")
    private Role role;
    private String accountNonLocked;
    private Meeting meeting;
}
