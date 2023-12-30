package know_wave.comma.config.security.config;

import know_wave.comma.config.security.service.JwtLogoutHandler;
import know_wave.comma.config.security.filter.LoggingFilter;
import know_wave.comma.config.security.filter.JwtAuthenticationFilter;
import know_wave.comma.config.security.entity.Role;
import know_wave.comma.config.security.service.ResponseAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoggingFilter loggingFilter;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final ResponseAccessDeniedHandler responseAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .sessionManagement(SessionManagementConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers("/admin/account/*").hasRole(Role.ADMIN.name())
                                .requestMatchers("/admin/**").hasAnyRole(Role.MANAGER.name(), Role.ADMIN.name())
                                .requestMatchers("/account/signin", "/account/email/verify/request", "/account/email/verify", "/account/signup", "/account/refresh-token").permitAll()
                                .requestMatchers(HttpMethod.GET, "/account/**").authenticated()
                                .requestMatchers(HttpMethod.GET).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers("/**").authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
//                .addFilterBefore(loggingFilter, JwtAuthenticationFilter.class)
                .logout(logout -> {
                    // 로그아웃 시 브라우저가 헤더, 캐시, 쿠키 등 모든 정보를 삭제하도록 Clear-Site-Data HTTP 헤더를 추가
                    HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.ALL));

                    logout
                            .logoutUrl("/account/signout")
                            .addLogoutHandler(jwtLogoutHandler)
                            .addLogoutHandler(clearSiteData)
                            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                })
                .exceptionHandling(handler ->
                        handler
                                .accessDeniedHandler(responseAccessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder,
//                                                       UserDetailsService userDetailsService,
//                                                       PasswordEncoder passwordEncoder) throws Exception {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        builder.authenticationProvider(authenticationProvider);
//        return builder.build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder);
//        return new ProviderManager(authenticationProvider);
//    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.debug(true);
//    }

}
