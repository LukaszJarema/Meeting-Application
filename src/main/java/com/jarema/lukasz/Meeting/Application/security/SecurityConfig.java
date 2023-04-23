package com.jarema.lukasz.Meeting.Application.security;

/*
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/loginAsAVisitor", "/visitors/new")
                .permitAll()
                .and()
                .formLogin(form -> form
                        .loginPage("/loginAsAVisitor")
                        .defaultSuccessUrl("/meeting")
                        .loginProcessingUrl("/loginAsAVisitor")
                        .failureUrl("/loginAsAVisitor?error=true")
                        .permitAll()
                ).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());
        return http.build();
    }
}

 */
