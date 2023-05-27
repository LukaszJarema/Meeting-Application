package com.jarema.lukasz.Meeting.Application.dtos;

import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MeetingDto {
    private Long id;
    @NotEmpty(message = "Content could not be empty")
    private String contentOfMeeting;
    @FutureOrPresent
    private LocalDateTime startOfMeeting;
    private LocalDateTime endOfMeeting;
    private Status status;
    private Employee employee;
    private Visitor visitor;
}
