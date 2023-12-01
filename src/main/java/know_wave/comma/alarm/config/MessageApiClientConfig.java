package know_wave.comma.alarm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class MessageApiClientConfig {

    @Value("${kakao.api.message.key}")
    private String kakaoMessageApiKey;

    @Bean
    public RestTemplate kakaotalkMessageApiClient() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor httpRequestInterceptor = (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + kakaoMessageApiKey);
            return execution.execute(request, body);
        };

        restTemplate.setInterceptors(Collections.singletonList(httpRequestInterceptor));
        return restTemplate;
    }
}
