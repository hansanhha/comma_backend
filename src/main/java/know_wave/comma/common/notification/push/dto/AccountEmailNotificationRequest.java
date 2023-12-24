package know_wave.comma.common.notification.push.dto;

import jakarta.annotation.Nullable;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEmailNotificationRequest {

    public static AccountEmailNotificationRequest create(String dest, String title, String content) {
        return new AccountEmailNotificationRequest(dest, title, content);
    }

    private final String dest;
    private final String title;
    private final String content;
}
