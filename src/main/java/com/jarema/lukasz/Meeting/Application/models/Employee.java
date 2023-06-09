package com.jarema.lukasz.Meeting.Application.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name could not be empty")
    private String name;
    @NotEmpty(message = "Surname could not be empty")
    private String surname;
    @NotEmpty(message = "Email address could not be empty")
    @Email(message = "Email is not valid")
    private String emailAddress;
    @NotEmpty(message = "Department could not be empty")
    private String department;
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;
    @Pattern(regexp = "^[0-9]{9}$", message = "Telephone number should be contains 9 digits")
    private String telephoneNumber;
    @NotEmpty(message = "Choose one role for an employee")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_role",
            joinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> role = new HashSet<>();
    private String accountNonLocked;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_meeting",
            joinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "meeting_id", referencedColumnName = "id")})
    private List<Meeting> meeting = new ArrayList<>();
}
