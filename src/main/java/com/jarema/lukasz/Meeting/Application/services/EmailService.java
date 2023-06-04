package com.jarema.lukasz.Meeting.Application.services;

import java.time.LocalDateTime;

public interface EmailService {
    void sendMail(String to, String subject, String text);
    void sendInformationAboutMeetingToEmployee(String emailAddress, String visitor, String content, LocalDateTime date);
    void sendConfirmationOfMeetingToVisitor(String emailAddress);
    void sendInformationAboutDeletedMeetingToEmployee(String emailAddress, String visitor, String content,
                                                      LocalDateTime date);

    void sendConfirmationAboutDeletedMeetingToVisitor(String emailAddress);
}
