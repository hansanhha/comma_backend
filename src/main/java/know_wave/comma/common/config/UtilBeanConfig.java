package know_wave.comma.common.config;

import know_wave.comma.common.message.FIeldNameMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class UtilBeanConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

        factoryBean.setMessageInterpolator(new FIeldNameMessageInterpolator());

        return factoryBean;
    }

//    @Bean
//    public ConstraintValueMessageInterpolator constraintValueMessageInterpolator() {
//        return new ConstraintValueMessageInterpolator();
//    }
}
