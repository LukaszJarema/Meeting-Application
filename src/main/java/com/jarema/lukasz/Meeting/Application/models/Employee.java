package com.jarema.lukasz.Meeting.Application.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String emailAddress;
    private String department;
    private String password;
    private String telephoneNumber;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private Set<Meeting> meetings = new HashSet<>();
}
