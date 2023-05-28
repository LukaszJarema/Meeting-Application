package com.jarema.lukasz.Meeting.Application.services;

public interface EmailService {
    void sendMail(String[] to, String subject, String text);
}
