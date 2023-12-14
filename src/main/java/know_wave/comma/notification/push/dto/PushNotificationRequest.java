package know_wave.comma.notification.push.dto;

import know_wave.comma.notification.base.entity.NotificationFeature;
import know_wave.comma.notification.push.entity.PushNotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PushNotificationRequest {

    public static PushNotificationRequest to(Map<PushNotificationType, String> destMap, String title, String content, NotificationFeature alarmFeature, Map<Object, Object> dataMap) {
        return new PushNotificationRequest(destMap, title, content, alarmFeature, dataMap);
    }

    private final Map<PushNotificationType, String> destMap;
    private final String title;
    private final String content;
    private final NotificationFeature notificationFeature;
    private final Map<Object, Object> dataMap;

}
