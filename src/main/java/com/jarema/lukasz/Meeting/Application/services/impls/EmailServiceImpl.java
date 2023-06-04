package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("{spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);

            javaMailSender.send(mimeMessage);
            System.out.println("Email has been sent");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendInformationAboutMeetingToEmployee(String emailAddress, String visitor, String content, LocalDateTime date) {
        String text = "Hello!\n\nYou receive new invitation for a meeting from: " + visitor +
                "\nMeeting will take place: " + date + "\nContent of the meeting will be: " + content +
                "\n\nThe meeting is currently rejected but if you or anyone else invited wants it to take place you " +
                "must log into the Meeting Application portal and accept it.\n" +
                "Please note that if a member of staff has already accepted it in the system, you can join the " +
                "meeting without accepting it.\n\n" +
                "This email has been sent from Meeting Application of your company.";
        sendMail(emailAddress, "New invitation for a meeting", text);
    }

    public void sendConfirmationOfMeetingToVisitor(String emailAddress) {
        String text = "Hello!\n\nYour invitation has been sent to our employees. If someone from them accept it you " +
                "will receive an email with information about that.\n\n" +
                "This email has been sent from Meeting Application of our company.";
        sendMail(emailAddress, "Invitation has been sent to our employee(s)", text);
    }
}
