package know_wave.comma.web.config;

import know_wave.comma.config.security.filter.JwtAuthenticationFilter;
import know_wave.comma.config.security.service.JwtTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static know_wave.comma.config.security.filter.JwtAuthenticationFilter.AUTHORIZATION_HEADER;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders(AUTHORIZATION_HEADER)
                .allowCredentials(true).maxAge(6000);
    }
}
