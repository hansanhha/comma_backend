package know_wave.comma.notification.alarm.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaotalkSender implements AlarmSender {

    private final RestTemplate kakaoApiClient;

    public KakaotalkSender(@Qualifier("kakaotalkMessageApiClient") RestTemplate kakaoApiClient) {
        this.kakaoApiClient = kakaoApiClient;
    }

    @Override
    public void send(String dest, String title, String message, Map<String, Object> param) {

    }
}
