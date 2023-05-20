package com.jarema.lukasz.Meeting.Application.security;

import com.jarema.lukasz.Meeting.Application.services.impls.CustomEmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class EmployeeSecurityConfig {

    private CustomEmployeeDetailsService customEmployeeDetailsService;

    @Autowired
    public EmployeeSecurityConfig(CustomEmployeeDetailsService customEmployeeDetailsService) {
        this.customEmployeeDetailsService = customEmployeeDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider1() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customEmployeeDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain employeeSecurityFiletChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider1());

        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/employeeLogin").permitAll()
                .requestMatchers("/admins/**").hasAuthority("ADMINISTRATOR")
                .requestMatchers("/employees/**").hasAuthority("EMPLOYEE")
                .requestMatchers("/receptionists/**").hasAuthority("RECEPTIONIST")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/employeeLogin")
                .usernameParameter("emailAddress")
                .defaultSuccessUrl("/")
                .failureUrl("/employeeLogin?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied")
                .and()
                .formLogin()
                .successHandler(employeeSuccessHandler())
                .failureHandler(employeeFailureHandler());

        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customEmployeeDetailsService).passwordEncoder(passwordEncoder());
    }

    private AuthenticationSuccessHandler employeeSuccessHandler() {
        return ((request, response, authentication) -> {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                if (auth.getAuthority().equals("ADMINISTRATOR")) {
                    response.sendRedirect("/admins/welcome");
                    return;
                } else if (auth.getAuthority().equals("EMPLOYEE")) {
                    response.sendRedirect("/employees/welcome");
                    return;
                } else if (auth.getAuthority().equals("RECEPTIONIST")) {
                    response.sendRedirect("/receptionists/welcome");
                    return;
                }
            }
        });
    }

    private AuthenticationFailureHandler employeeFailureHandler() {
        return ((request, response, exception) -> {
            String errorMessage = "Błędny login lub hasło";
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/employeeLogin?error");
        });
    }
}
