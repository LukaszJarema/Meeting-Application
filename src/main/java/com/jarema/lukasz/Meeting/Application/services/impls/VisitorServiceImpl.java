package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {
    private VisitorRepository visitorRepository;
    @Override
    public Visitor saveVisitor(VisitorDto visitorDto) {
        Visitor visitor = mapToVisitor(visitorDto);
        return visitorRepository.save(visitor);
    }

    private Visitor mapToVisitor(VisitorDto visitorDto) {
        Visitor visitor = Visitor.builder()
                .id(visitorDto.getId())
                .name(visitorDto.getName())
                .surname(visitorDto.getSurname())
                .emailAddress(visitorDto.getEmailAddress())
                .telephoneNumber(visitorDto.getTelephoneNumber())
                .password(visitorDto.getPassword())
                .build();
        return visitor;
    }
}
