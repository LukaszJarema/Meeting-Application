package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;

public interface MeetingService {
    void createMeeting(Long employeeId, MeetingDto meetingDto);
}
