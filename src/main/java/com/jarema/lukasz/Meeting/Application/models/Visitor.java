package com.jarema.lukasz.Meeting.Application.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String emailAddress;
    private String password;
    private String telephoneNumber;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "visitor_role", joinColumns = {@JoinColumn(name = "visitor_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> role = new ArrayList<>();
    @OneToMany(mappedBy = "visitor", cascade = CascadeType.REMOVE)
    private List<Meeting> meetings = new ArrayList<>();
}