package com.jarema.lukasz.Meeting.Application.dtos;

import com.jarema.lukasz.Meeting.Application.enums.SupportStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupportDto {
    private Long id;
    private String emailAddress;
    private String message;
    private String answer;
    private SupportStatus supportStatus;
}
