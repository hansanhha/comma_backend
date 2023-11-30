package know_wave.comma.message.config;

import know_wave.comma.message.util.FieldNameMessageInterpolator;
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
