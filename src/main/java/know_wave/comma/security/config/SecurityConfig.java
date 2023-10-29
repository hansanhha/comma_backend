package know_wave.comma.security.config;

import know_wave.comma.account.service.auth.LogoutService;
import know_wave.comma.security.filter.JwtAuthenticationFilter;
import know_wave.comma.security.filter.LoggingFilter;
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

import static know_wave.comma.account.entity.auth.Role.ADMIN;
import static know_wave.comma.account.entity.auth.Role.MANAGER;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoggingFilter loggingFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PermitRequestMatcherConfig permitRequestMatcherConfig;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, LoggingFilter loggingFilter, AuthenticationProvider authenticationProvider, LogoutService logoutService, AccessDeniedHandler accessDeniedHandler, PermitRequestMatcherConfig permitRequestMatcherConfig) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loggingFilter = loggingFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutService = logoutService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.permitRequestMatcherConfig = permitRequestMatcherConfig;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(permitRequestMatcherConfig.getAdminPermitRequestMatchers().toArray(new RequestMatcher[0])).permitAll()
                                .requestMatchers("/admin/account/*").hasRole(ADMIN.name())
                                .requestMatchers("/admin/**").hasAnyRole(MANAGER.name(), ADMIN.name())
                                .requestMatchers(permitRequestMatcherConfig.getUserPermitRequestMatchers().toArray(new RequestMatcher[0])).permitAll()
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
