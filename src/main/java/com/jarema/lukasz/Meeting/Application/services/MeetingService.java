package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;
import com.jarema.lukasz.Meeting.Application.models.Meeting;

public interface MeetingService {
    void createMeeting(Long visitorId, MeetingDto meetingDto);
    void delete(Long meetingId);
    void updateMeeting(Long visitorId, Meeting meeting);
}
