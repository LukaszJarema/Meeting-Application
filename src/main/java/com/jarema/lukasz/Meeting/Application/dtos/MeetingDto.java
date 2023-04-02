package com.jarema.lukasz.Meeting.Application.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MeetingDto {
    private Long id;
    private String contentOfMeeting;
    private LocalDateTime startOfMeeting;
    private LocalDateTime endOfMeeting;
}
