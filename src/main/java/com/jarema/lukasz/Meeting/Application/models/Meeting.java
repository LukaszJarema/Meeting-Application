package com.jarema.lukasz.Meeting.Application.models;

import com.jarema.lukasz.Meeting.Application.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name could not be empty")
    private String contentOfMeeting;
    @FutureOrPresent(message = "The start of meeting date can not be from the past")
    private LocalDateTime startOfMeeting;
    @Future(message = "The end of meeting date can not be earlier that start of meeting")
    private LocalDateTime endOfMeeting;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToMany(mappedBy = "meeting")
    private List<Employee> employees = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "visitor_id", nullable = false)
    private Visitor visitor;
}
