package com.jarema.lukasz.Meeting.Application.models;

import com.jarema.lukasz.Meeting.Application.enums.SupportStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "Support")
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Email address could not be empty")
    @Email(message = "Email is not valid")
    private String emailAddress;
    @Size(min = 25, message = "Message should be at least 30 characters long")
    private String message;
    private String answer;
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime closedAd;
}
