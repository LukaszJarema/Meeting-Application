package com.jarema.lukasz.Meeting.Application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VisitorDto {
    private Long id;
    @NotEmpty(message = "Name could not be empty")
    private String name;
    @NotEmpty(message = "Surname could not be empty")
    private String surname;
    @NotEmpty(message = "Email address could not be empty")
    @Email(message = "Email is not valid")
    private String emailAddress;
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
    @Pattern(regexp = "^[0-9]{9}$", message = "Telephone number should be contains 9 digits")
    private String telephoneNumber;
    private String accountNonLocked;
    private List<MeetingDto> meetings;
}
