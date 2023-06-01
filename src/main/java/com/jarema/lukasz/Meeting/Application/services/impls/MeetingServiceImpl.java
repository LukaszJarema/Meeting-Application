package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;
import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingServiceImpl implements MeetingService {
    private MeetingRepository meetingRepository;
    private EmployeeRepository employeeRepository;
    private VisitorRepository visitorRepository;

    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository, EmployeeRepository employeeRepository,
                              VisitorRepository visitorRepository) {
        this.meetingRepository = meetingRepository;
        this.employeeRepository = employeeRepository;
        this.visitorRepository = visitorRepository;
    }

    private Meeting mapToMeeting(MeetingDto meetingDto) {
        return Meeting.builder()
                .id(meetingDto.getId())
                .contentOfMeeting(meetingDto.getContentOfMeeting())
                .startOfMeeting(meetingDto.getStartOfMeeting())
                .endOfMeeting(meetingDto.getEndOfMeeting())
                .status(Status.valueOf(String.valueOf(Status.REJECTED)))
                .employees(meetingDto.getEmployees())
                .build();
    }

    @Override
    public void createMeeting(Long visitorId, MeetingDto meetingDto) {
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        Meeting meeting = mapToMeeting(meetingDto);
        meeting.setVisitor(visitor);
        meeting.setEmployees(meetingDto.getEmployees());
        meetingRepository.save(meeting);

    }
}
