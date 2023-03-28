package com.jarema.lukasz.Meeting.Application.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameOfVisitor;
    private String surnameOfVisitor;
    private String contentOfMeeting;
    private String visitorEmailAddress;
    private String visitorTelephoneNumber;
    private LocalDateTime startOfMeeting;
    private LocalDateTime endOfMeeting;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
