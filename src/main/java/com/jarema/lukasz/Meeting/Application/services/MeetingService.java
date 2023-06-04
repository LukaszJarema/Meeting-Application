package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;

public interface MeetingService {
    void createMeeting(Long visitorId, MeetingDto meetingDto);
    void delete(Long meetingId);
}
