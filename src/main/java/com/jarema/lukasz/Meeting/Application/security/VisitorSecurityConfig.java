package com.jarema.lukasz.Meeting.Application.security;


import com.jarema.lukasz.Meeting.Application.services.impls.CustomVisitorDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class VisitorSecurityConfig {

    private CustomVisitorDetailsService customVisitorDetailsService;
    private EmployeeSecurityConfig employeeSecurityConfig;

    public VisitorSecurityConfig(CustomVisitorDetailsService customVisitorDetailsService, EmployeeSecurityConfig employeeSecurityConfig) {
        this.customVisitorDetailsService = customVisitorDetailsService;
        this.employeeSecurityConfig = employeeSecurityConfig;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider2() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customVisitorDetailsService);
        authProvider.setPasswordEncoder(employeeSecurityConfig.passwordEncoder());
        return authProvider;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain visitorSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider2());

        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .requestMatchers( "/visitorLogin", "/register").permitAll()
                .requestMatchers("/visitors/**").hasAuthority("VISITOR")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/visitorLogin")
                .usernameParameter("emailAddress")
                .defaultSuccessUrl("/")
                .failureUrl("/visitorLogin?error")
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
                .successHandler(visitorSuccessHandler())
                .failureHandler(visitorFailureHandler());

        return http.build();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customVisitorDetailsService).passwordEncoder(employeeSecurityConfig.passwordEncoder());
    }

    private AuthenticationSuccessHandler visitorSuccessHandler() {
        return ((request, response, authentication) -> {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                if (auth.getAuthority().equals("VISITOR")) {
                    response.sendRedirect("/visitors/welcome");
                    return;
                }
            }
        });
    }

    private AuthenticationFailureHandler visitorFailureHandler() {
        return ((request, response, exception) -> {
            String errorMessage = "Błędny login lub hasło";
            request.getSession().setAttribute("errorMessage", errorMessage);
            response.sendRedirect("/visitorLogin?error");
        });
    }
}
