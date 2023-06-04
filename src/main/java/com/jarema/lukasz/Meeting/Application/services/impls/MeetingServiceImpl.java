package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;
import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {
    private MeetingRepository meetingRepository;
    private EmployeeRepository employeeRepository;
    private VisitorRepository visitorRepository;
    private EmailService emailService;

    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository, EmployeeRepository employeeRepository,
                              VisitorRepository visitorRepository, EmailService emailService) {
        this.meetingRepository = meetingRepository;
        this.employeeRepository = employeeRepository;
        this.visitorRepository = visitorRepository;
        this.emailService = emailService;
    }

    private Meeting mapToMeeting(MeetingDto meetingDto) {
        return Meeting.builder()
                .id(meetingDto.getId())
                .contentOfMeeting(meetingDto.getContentOfMeeting())
                .startOfMeeting(meetingDto.getStartOfMeeting())
                .endOfMeeting(meetingDto.getEndOfMeeting())
                .status(Status.valueOf(String.valueOf(Status.REJECTED)))
                .build();
    }

    @Override
    public void createMeeting(Long visitorId, MeetingDto meetingDto) {
        Visitor visitor = visitorRepository.findById(visitorId).orElse(null);
        Meeting meeting = mapToMeeting(meetingDto);
        meeting.setVisitor(visitor);
        List<Employee> selectedEmployees = employeeRepository.findAllById(meetingDto.getEmployeeIds());
        meeting.setEmployees(selectedEmployees);
        for (Employee employee : selectedEmployees) {
            employee.getMeeting().add(meeting);
            String emailAddress = employee.getEmailAddress();
            String visitorNameAndSurname = visitor.getName() + " " + visitor.getSurname();
            String content = meeting.getContentOfMeeting();
            LocalDateTime dateTime = meeting.getStartOfMeeting();
            emailService.sendInformationAboutMeetingToEmployee(emailAddress, visitorNameAndSurname, content, dateTime);
        }
        String visitorEmailAddress = visitor.getEmailAddress();
        emailService.sendConfirmationOfMeetingToVisitor(visitorEmailAddress);
        meetingRepository.save(meeting);
    }
}
