package com.jarema.lukasz.Meeting.Application.dtos;

import com.jarema.lukasz.Meeting.Application.enums.SupportStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SupportDto {
    private Long id;
    @NotEmpty(message = "Email address could not be empty")
    @Email(message = "Email is not valid")
    private String emailAddress;
    @Size(min = 25, message = "Message should be at least 30 characters long")
    private String message;
    private String answer;
    private SupportStatus supportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime closedAd;
}
