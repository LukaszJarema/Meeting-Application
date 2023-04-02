package com.jarema.lukasz.Meeting.Application.models;

import com.jarema.lukasz.Meeting.Application.models.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
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
    @OneToMany(mappedBy = "visitor", cascade = CascadeType.REMOVE)
    private Set<Meeting> meetings = new HashSet<>();
}
