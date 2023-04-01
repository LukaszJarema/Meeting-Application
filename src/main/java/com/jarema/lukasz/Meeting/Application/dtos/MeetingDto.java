package com.jarema.lukasz.Meeting.Application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDto {
    private Long id;
    private String contentOfMeeting;
    private LocalDateTime startOfMeeting;
    private LocalDateTime endOfMeeting;
}
