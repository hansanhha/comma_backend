package know_wave.comma.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class PaymentApiClientConfig {

    @Value("${kakao.api.pay.key}")
    private String kakaoPayApiKey;

    @Bean
    public RestTemplate kakaoPayApiClient() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor httpRequestInterceptor = (request, body, execution) -> {
            request.getHeaders().add("Authorization", "KakaoAK " + kakaoPayApiKey);
            return execution.execute(request, body);
        };

        restTemplate.setInterceptors(Collections.singletonList(httpRequestInterceptor));
        return restTemplate;
    }
}
