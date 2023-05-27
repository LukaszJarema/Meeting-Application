package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingServiceImpl implements MeetingService {
    private MeetingRepository meetingRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository, EmployeeRepository employeeRepository) {
        this.meetingRepository = meetingRepository;
        this.employeeRepository = employeeRepository;
    }

    private Meeting mapToMeeting(MeetingDto meetingDto) {
        return Meeting.builder()
                .id(meetingDto.getId())
                .contentOfMeeting(meetingDto.getContentOfMeeting())
                .startOfMeeting(meetingDto.getStartOfMeeting())
                .endOfMeeting(meetingDto.getEndOfMeeting())
                .build();
    }

    @Override
    public void createMeeting(Long employeeId, MeetingDto meetingDto) {

    }
}
