package know_wave.comma.common.notification.push.service;

import know_wave.comma.common.notification.push.entity.PushNotificationType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class KakaotalkSender implements PushNotificationSender {

    private static final PushNotificationType NOTIFICATION_KAKAOTALK_TYPE = PushNotificationType.KAKAOTALK;

    @Override
    public void send(String dest, String title, String content, Map<Object, Object> paramMap) {

    }

    @Override
    public boolean isSupport(PushNotificationType type) {
        return NOTIFICATION_KAKAOTALK_TYPE == type;
    }
}
