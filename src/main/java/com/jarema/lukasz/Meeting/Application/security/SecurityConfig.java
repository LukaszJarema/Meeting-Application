package com.jarema.lukasz.Meeting.Application.security;

import com.jarema.lukasz.Meeting.Application.services.impls.CustomEmployeeDetailsService;
import com.jarema.lukasz.Meeting.Application.services.impls.CustomVisitorDetailsService;
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
public class SecurityConfig {

    private CustomEmployeeDetailsService customEmployeeDetailsService;

    private CustomVisitorDetailsService customVisitorDetailsService;

    @Autowired
    public SecurityConfig(CustomEmployeeDetailsService customEmployeeDetailsService,
                          CustomVisitorDetailsService customVisitorDetailsService) {
        this.customEmployeeDetailsService = customEmployeeDetailsService;
        this.customVisitorDetailsService = customVisitorDetailsService;
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
    public DaoAuthenticationProvider authenticationProvider2() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customVisitorDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain employeeSecurityFiletChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider1());
        http.authenticationProvider(authenticationProvider2());

        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/login", "/register").permitAll()
                .requestMatchers("/admins/**").hasAuthority("ADMINISTRATOR")
                .requestMatchers("/employees/**").hasAuthority("EMPLOYEE")
                .requestMatchers("/receptionists/**").hasAuthority("RECEPTIONIST")
                .requestMatchers("/visitors/**").hasAuthority("VISITOR")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("emailAddress")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
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
                    response.sendRedirect("/admins/home");
                    return;
                } else if (auth.getAuthority().equals("EMPLOYEE")) {
                    response.sendRedirect("/employees/home");
                    return;
                } else if (auth.getAuthority().equals("RECEPTIONIST")) {
                    response.sendRedirect("/receptionists/home");
                    return;
                } else if (auth.getAuthority().equals("VISITOR")) {
                    response.sendRedirect("/visitors/home");
                    return;
                }
            }
        });
    }

    private AuthenticationFailureHandler employeeFailureHandler() {
        return ((request, response, exception) -> {
            String errorMessage = "Błędny login lub hasło";
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/login?error");
        });
    }
}
