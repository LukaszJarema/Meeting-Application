package com.jarema.lukasz.Meeting.Application.services;

import com.jarema.lukasz.Meeting.Application.dtos.SupportDto;
import com.jarema.lukasz.Meeting.Application.models.Support;

public interface SupportService {
    Support saveSupport(SupportDto supportDto);
}
