package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {
    private VisitorRepository visitorRepository;

    @Autowired
    public VisitorServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public Visitor saveVisitor(VisitorDto visitorDto) {
        Visitor visitor = mapToVisitor(visitorDto);
        return visitorRepository.save(visitor);
    }

    private Visitor mapToVisitor(VisitorDto visitor) {
        Visitor visitorDto = Visitor.builder()
                .id(visitor.getId())
                .name(visitor.getName())
                .surname(visitor.getSurname())
                .emailAddress(visitor.getEmailAddress())
                .password(visitor.getPassword())
                .telephoneNumber(visitor.getTelephoneNumber())
                .build();
        return visitorDto;
    }
}
