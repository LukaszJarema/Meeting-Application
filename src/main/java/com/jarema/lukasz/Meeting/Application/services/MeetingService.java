package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;

public interface MeetingService {
    void createMeeting(Long visitorId, Long[] employeeId, MeetingDto meetingDto);
}
