package com.jarema.lukasz.Meeting.Application.details;

import com.jarema.lukasz.Meeting.Application.models.Visitor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomVisitorDetails implements UserDetails {

    private Visitor visitor;

    public CustomVisitorDetails(Visitor visitor) {
        this.visitor = visitor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(visitor.getRole().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return visitor.getPassword();
    }

    @Override
    public String getUsername() {
        return visitor.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
