package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Visitor;

public interface VisitorService {
    Visitor saveVisitor(VisitorDto visitorDto);
    Visitor findByEmail(String emailAddress);
}
