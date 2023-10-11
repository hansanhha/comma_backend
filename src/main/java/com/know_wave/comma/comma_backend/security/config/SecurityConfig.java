package com.know_wave.comma.comma_backend.security.config;

import com.know_wave.comma.comma_backend.account.service.auth.LogoutService;
import com.know_wave.comma.comma_backend.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.know_wave.comma.comma_backend.account.entity.auth.Role.ADMIN;
import static com.know_wave.comma.comma_backend.account.entity.auth.Role.MANAGER;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final HandlerMappingIntrospector handlerIntroceptor;
    private RequestMatcher[] userPermitRequestMatchers;
    private RequestMatcher[] adminPermitRequestMatchers;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider, LogoutService logoutService, AccessDeniedHandler accessDeniedHandler, HandlerMappingIntrospector handlerIntroceptor) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutService = logoutService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.handlerIntroceptor = handlerIntroceptor;
        initPermitRequests();
    }

    private void initPermitRequests() {

        MvcRequestMatcher getArduino = new MvcRequestMatcher(handlerIntroceptor, "/arduino/*");
        getArduino.setMethod(HttpMethod.GET);
        MvcRequestMatcher getArduinoList = new MvcRequestMatcher(handlerIntroceptor, "/arduinos/**");
        getArduinoList.setMethod(HttpMethod.GET);

        userPermitRequestMatchers = new MvcRequestMatcher[]{
                new MvcRequestMatcher(handlerIntroceptor, "/account/signin"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/signup"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/r"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/verify"),
                getArduino,
                getArduinoList,
        };

        adminPermitRequestMatchers = new MvcRequestMatcher[]{
                new MvcRequestMatcher(handlerIntroceptor, "/admin/account/signup")
        };


        // custom filter permit requests configuration
        jwtAuthenticationFilter.requestMatchers(
                Stream.concat(
                        Arrays.stream(userPermitRequestMatchers),
                        Arrays.stream(adminPermitRequestMatchers)
                ).toList()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeHttpRequest ->
                authorizeHttpRequest
                        .requestMatchers(adminPermitRequestMatchers).permitAll()
                        .requestMatchers("/admin/account/*").hasRole(ADMIN.name())
                        .requestMatchers("/admin/**").hasAnyRole(MANAGER.name(), ADMIN.name())
                        .requestMatchers(userPermitRequestMatchers).permitAll()
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
