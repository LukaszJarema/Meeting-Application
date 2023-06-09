package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Visitor;

import java.util.List;

public interface VisitorService {
    Visitor saveVisitor(VisitorDto visitorDto);
    Visitor findByEmail(String emailAddress);
    public Long getVisitorIdByLoggedInInformation();
    void updateVisitor(VisitorDto visitor);
    List<VisitorDto> searchVisitorsByNameOrSurname(String query);
    VisitorDto findVisitorById(Long visitorId);
    Visitor findById(Long visitorId);
}
