package com.know_wave.comma.comma_backend.security.config;

import com.know_wave.comma.comma_backend.account.service.LogoutService;
import com.know_wave.comma.comma_backend.security.filter.JwtAuthenticationFilter;
import com.know_wave.comma.comma_backend.security.service.AccountAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.know_wave.comma.comma_backend.account.entity.auth.Role.ADMIN;
import static com.know_wave.comma.comma_backend.account.entity.auth.Role.MANAGER;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final String[] accountPermitRequest = {"/account/signin", "/account/signup", "/account/email/r", "/account/email/verify"};
    private final String[] adminPermitRequest = {"/admin/signup"};

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider, LogoutService logoutService, AccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutService = logoutService;
        this.accessDeniedHandler = accessDeniedHandler;
        initJwtAuthenticationFilter();
    }

    private void initJwtAuthenticationFilter() {
        jwtAuthenticationFilter.requestMatchers(
                Stream.concat(
                        Arrays.stream(accountPermitRequest),
                        Arrays.stream(adminPermitRequest))
                        .toArray(String[]::new)
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeHttpRequest ->
                authorizeHttpRequest
                        .requestMatchers("/admin/signup").permitAll()
                        .requestMatchers("/admin/manager").hasRole(ADMIN.name())
                        .requestMatchers("/admin/**").hasAnyRole(MANAGER.name(), ADMIN.name())
                        .requestMatchers(accountPermitRequest).permitAll()
                        .requestMatchers("/**").authenticated()
            )
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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
