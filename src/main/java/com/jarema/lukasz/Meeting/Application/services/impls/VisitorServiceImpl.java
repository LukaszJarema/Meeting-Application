package com.jarema.lukasz.Meeting.Application.services.impls;

import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class VisitorServiceImpl implements VisitorService {
    private VisitorRepository visitorRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public VisitorServiceImpl(VisitorRepository visitorRepository, RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.visitorRepository = visitorRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Visitor saveVisitor(VisitorDto visitorDto) {
        Visitor visitor = mapToVisitor(visitorDto);
        Role role = roleRepository.findByName("VISITOR");
        visitor.setRole(Arrays.asList(role));
        return visitorRepository.save(visitor);
    }

    @Override
    public Visitor findByEmail(String emailAddress) {
        return visitorRepository.findByEmailAddress(emailAddress);
    }

    private Visitor mapToVisitor(VisitorDto visitor) {
        Visitor visitorDto = Visitor.builder()
                .id(visitor.getId())
                .name(visitor.getName())
                .surname(visitor.getSurname())
                .emailAddress(visitor.getEmailAddress())
                .password(passwordEncoder.encode(visitor.getPassword()))
                .telephoneNumber(visitor.getTelephoneNumber())
                .build();
        return visitorDto;
    }

    public Long getVisitorIdByLoggedInInformation() {
        String nameOfVisitor;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            nameOfVisitor = ((UserDetails)principal).getUsername();
        } else {
            nameOfVisitor = principal.toString();
        }
        Long id = visitorRepository.findByEmailAddress(nameOfVisitor).getId();
        return id;
    }

    @Override
    public void updateVisitor(VisitorDto visitorDto) {
       Visitor visitor = mapToVisitor(visitorDto);
       Role role = roleRepository.findByName("VISITOR");
       visitor.setRole(Arrays.asList(role));
       visitor.setPassword(visitorRepository.findById(visitor.getId()).get().getPassword());
       visitorRepository.save(visitor);
    }
}
