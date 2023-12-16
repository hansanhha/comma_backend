package know_wave.comma.common.security.config;

import know_wave.comma.common.security.service.TokenLogoutService;
import know_wave.comma.common.security.filter.LoggingFilter;
import know_wave.comma.common.security.filter.JwtAuthenticationFilter;
import know_wave.comma.common.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LoggingFilter loggingFilter;
    private final TokenLogoutService tokenLogoutService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PermitRequestMatcherConfig permitRequestMatcherConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(permitRequestMatcherConfig.getAdminPermitRequestMatchers().toArray(new RequestMatcher[0])).permitAll()
                                .requestMatchers("/admin/account/*").hasRole(Role.ADMIN.name())
                                .requestMatchers("/admin/**").hasAnyRole(Role.MANAGER.name(), Role.ADMIN.name())
                                .requestMatchers(permitRequestMatcherConfig.getUserPermitRequestMatchers().toArray(new RequestMatcher[0])).permitAll()
                                .requestMatchers("/**").authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .requestCache(RequestCacheConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loggingFilter, JwtAuthenticationFilter.class)
                .logout(logout -> {

                    // 로그아웃 시 브라우저가 헤더, 캐시, 쿠키 등 모든 정보를 삭제하도록 Clear-Site-Data HTTP 헤더를 추가
                    HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter());

                    logout
                            .logoutUrl("/account/signout")
                            .addLogoutHandler(tokenLogoutService)
                            .addLogoutHandler(clearSiteData)
                            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                })
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
