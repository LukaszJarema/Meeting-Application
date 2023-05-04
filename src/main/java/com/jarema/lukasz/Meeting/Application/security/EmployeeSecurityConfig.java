package com.jarema.lukasz.Meeting.Application.security;

import com.jarema.lukasz.Meeting.Application.services.impls.CustomAdministratorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class EmployeeSecurityConfig {

    private CustomAdministratorDetailsService customAdministratorDetailsService;

    @Autowired
    public EmployeeSecurityConfig(CustomAdministratorDetailsService customAdministratorDetailsService) {
        this.customAdministratorDetailsService = customAdministratorDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeHttpRequests().requestMatchers("/", "/login**", "/register").permitAll();

        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/employee/**").hasAuthority("EMPLOYEE")
                .and()
                .formLogin()
                .loginPage("/loginAsAnEmployee")
                .usernameParameter("emailAddress")
                .loginProcessingUrl("/loginAsAnEmployee")
                .defaultSuccessUrl("/employees/list")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");

        return http.build();
    }

}
