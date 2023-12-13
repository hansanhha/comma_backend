package know_wave.comma.notification.alarm.config;

import know_wave.comma.notification.alarm.util.FieldNameMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorMessageConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

        factoryBean.setMessageInterpolator(new FieldNameMessageInterpolator());

        return factoryBean;
    }
}
