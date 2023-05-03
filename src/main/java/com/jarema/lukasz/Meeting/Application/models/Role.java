package com.jarema.lukasz.Meeting.Application.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "role")
    private List<Employee> employees = new ArrayList<>();
    @ManyToMany(mappedBy = "role")
    private List<Visitor> visitors = new ArrayList<>();
}
