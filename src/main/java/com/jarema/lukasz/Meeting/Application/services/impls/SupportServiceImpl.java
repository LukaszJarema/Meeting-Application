package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.SupportDto;
import com.jarema.lukasz.Meeting.Application.enums.SupportStatus;
import com.jarema.lukasz.Meeting.Application.models.Support;
import com.jarema.lukasz.Meeting.Application.repositories.SupportRepository;
import com.jarema.lukasz.Meeting.Application.services.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportServiceImpl implements SupportService {

    private SupportRepository supportRepository;

    @Autowired
    public SupportServiceImpl(SupportRepository supportRepository) {
        this.supportRepository = supportRepository;
    }

    @Override
    public Support saveSupport(SupportDto supportDto) {
        Support support = mapToSupport(supportDto);
        support.setSupportStatus(SupportStatus.OPEN);
        return supportRepository.save(support);
    }

    private Support mapToSupport(SupportDto support) {
        Support supportDto = Support.builder()
                .id(support.getId())
                .emailAddress(support.getEmailAddress())
                .message(support.getMessage())
                .build();
        return supportDto;
    }
}
