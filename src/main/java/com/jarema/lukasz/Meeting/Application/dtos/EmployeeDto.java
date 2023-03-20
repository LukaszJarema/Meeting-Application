package com.jarema.lukasz.Meeting.Application.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDto {
    private Long id;
    private String name;
    private String surname;
    private String emailAddress;
    private String password;
    private int telephoneNumber;
}
