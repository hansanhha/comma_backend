package know_wave.comma.config.security.config;

import know_wave.comma.config.security.service.SecurityLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggingInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityLogInterceptor())
                .excludePathPatterns("/css/**", "/image/**", "/js/**");
    }
}
