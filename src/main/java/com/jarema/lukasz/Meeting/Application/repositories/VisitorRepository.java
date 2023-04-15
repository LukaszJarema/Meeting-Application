package com.jarema.lukasz.Meeting.Application.repositories;

import com.jarema.lukasz.Meeting.Application.models.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    Visitor findByEmailAddress(String emailAddress);
}
