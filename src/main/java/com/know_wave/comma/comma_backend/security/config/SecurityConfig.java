package com.know_wave.comma.comma_backend.security.config;

import com.know_wave.comma.comma_backend.account.service.auth.LogoutService;
import com.know_wave.comma.comma_backend.security.filter.JwtAuthenticationFilter;
import com.know_wave.comma.comma_backend.security.filter.LoggingFilter;
import com.know_wave.comma.comma_backend.security.service.PermitRequestMatcherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static com.know_wave.comma.comma_backend.account.entity.auth.Role.ADMIN;
import static com.know_wave.comma.comma_backend.account.entity.auth.Role.MANAGER;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoggingFilter loggingFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PermitRequestMatcherService permitRequestMatcherService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, LoggingFilter loggingFilter, AuthenticationProvider authenticationProvider, LogoutService logoutService, AccessDeniedHandler accessDeniedHandler, PermitRequestMatcherService permitRequestMatcherService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loggingFilter = loggingFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutService = logoutService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.permitRequestMatcherService = permitRequestMatcherService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(permitRequestMatcherService.getAdminPermitRequestMatchers().toArray(new RequestMatcher[0])).permitAll()
                                .requestMatchers("/admin/account/*").hasRole(ADMIN.name())
                                .requestMatchers("/admin/**").hasAnyRole(MANAGER.name(), ADMIN.name())
                                .requestMatchers(permitRequestMatcherService.getUserPermitRequestMatchers().toArray(new RequestMatcher[0])).permitAll()
                                .requestMatchers("/**").authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loggingFilter, JwtAuthenticationFilter.class)
                .logout(logout ->
                        logout
                                .logoutUrl("/account/signout")
                                .addLogoutHandler(logoutService)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
                .exceptionHandling(handler ->
                        handler
                                .accessDeniedHandler(accessDeniedHandler)
                );
        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.debug(true);
//    }

}
