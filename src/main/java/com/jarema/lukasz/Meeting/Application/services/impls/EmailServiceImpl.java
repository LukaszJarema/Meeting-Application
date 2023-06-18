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
                "\n\nThe meeting is waiting for respond but if you or anyone else invited wants it to take place you " +
                "must log into the Meeting Application portal and accept it. You can also reject the meeting\n" +
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

    public void sendInformationAboutDeletedMeetingToEmployee(String emailAddress, String visitor, String content,
                                                             LocalDateTime date) {
        String text = "Hello!\n\nVisitor " + visitor + " deleted the meeting in the content: " + content + " that " +
                "should have taken place: " + date + "\n\nThis email has been sent from Meeting Application of your " +
                "company.";
        sendMail(emailAddress, "Your meeting has been deleted", text);
    }

    @Override
    public void sendConfirmationAboutDeletedMeetingToVisitor(String emailAddress) {
        String text = "Hello\n\nYour invitation has been deleted and the information has been sent to our employees." +
                "\n\nThis email has been sent from Meeting Application of your company.";
        sendMail(emailAddress, "Your meeting has been deleted", text);
    }

    @Override
    public void sendConfirmationAboutChangedStatusOfMeeting(String emailAddress, String employee,
                                                            String content, String status) {
        String text = "Hello\n\nEmployee " + employee + " changed the status of meeting: " + content + " to: " + status
                + " .\n\nThis email has been sent from Meeting Application of our company.";
        sendMail(emailAddress, "Your meeting changed the status", text);
    }

    @Override
    public void sendWelcomeMessageForNewEmployee(String emailAddress, String password) {
        String text = "Hello\n\n.Your account has been created in our Meeting Application. You can log in to the portal" +
                " with below credentials:\nLogin: " + emailAddress + "\nPassword: " + password + "\n\nThis email" +
                " has been sent from Meeting Application of our company.";
        sendMail(emailAddress, "Welcome in Meeting Application", text);
    }

    @Override
    public void sendConfirmationAboutOpenTicket(String emailAddress) {
        String text = "Hello.\n\nYour ticket has been created.\n\nEmail has been sent from Meeting Application" +
                " of company.";
        sendMail(emailAddress, "Ticket has been created", text);
    }

    @Override
    public void closeTicket(String emailAddress, String answer) {
        String text = "Hello.\n\nYour ticket has been closed with answer:\n" + answer + "\nIf you have any further " +
                "question please open new ticket.\n\nEmail has been sent from Meeting Application of our company.";
        sendMail(emailAddress, "Your ticket has been closed", text);
    }

    @Override
    public void sendInformationAboutEditedMeeting(String emailAddress, String visitor, String content, LocalDateTime date) {
        String text = "Hello.\n\nVisitor: " + visitor + " edited your meeting. New details:\n" + content +
                " Start at: " + date + "\nStatus of meeting is rejected, if you want to participate please change the" +
                " status or check if someone changed it to accepted.\n\nEmail has been sent from Meeting Application" +
                " of our company.";
        sendMail(emailAddress, "Your meeting has been edited", text);
    }

    @Override
    public void sendInformationToVisitorAboutEditedMeeting(String emailAddress) {
        String text = "Hello.\n\nThe meeting has been edited by you. The information has been sent to our employees." +
                " Please wait for accept it by them.\n\nEmail has been sent from Meeting Application of our company.";
        sendMail(emailAddress, "Your meeting has been edited", text);
    }
}
