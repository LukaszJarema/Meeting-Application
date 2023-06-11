package com.jarema.lukasz.Meeting.Application.services;

import java.time.LocalDateTime;

public interface EmailService {
    void sendMail(String to, String subject, String text);
    void sendInformationAboutMeetingToEmployee(String emailAddress, String visitor, String content, LocalDateTime date);
    void sendConfirmationOfMeetingToVisitor(String emailAddress);
    void sendInformationAboutDeletedMeetingToEmployee(String emailAddress, String visitor, String content,
                                                      LocalDateTime date);
    void sendConfirmationAboutDeletedMeetingToVisitor(String emailAddress);
    void sendConfirmationAboutChangedStatusOfMeeting(String emailAddress, String employee, String content, String status);
    void sendWelcomeMessageForNewEmployee(String emailAddress, String password);
    void sendConfirmationAboutOpenTicket(String emailAddress);
    void closeTicket(String emailAddress, String answer);
    void sendInformationAboutEditedMeeting(String emailAddress, String visitor, String content, LocalDateTime date);
    void sendInformationToVisitorAboutEditedMeeting(String emailAddress);
}
