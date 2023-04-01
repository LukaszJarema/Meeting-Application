package com.jarema.lukasz.Meeting.Application.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
}
