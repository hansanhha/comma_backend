package know_wave.comma.notification.base.service;

import jakarta.transaction.Transactional;
import know_wave.comma.notification.base.entity.NotificationFeature;
import know_wave.comma.notification.push.dto.PushNotificationRequest;
import know_wave.comma.notification.push.entity.PushNotificationType;
import know_wave.comma.notification.push.service.PushNotificationLogger;
import know_wave.comma.notification.push.service.PushNotificationManager;
import know_wave.comma.notification.push.service.PushNotificationOptionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationGateway {

    private final PushNotificationOptionReader pushNotificationOptionReader;
    private final PushNotificationManager pushNotificationManager;
    private final PushNotificationLogger pushNotificationLogger;

    public void notify(PushNotificationRequest notificationRequest) {
        if (notificationRequest.getNotificationFeature() == NotificationFeature.ACCOUNT_AUTH_CODE) {
            authenticationNotification(notificationRequest);
        }

        if (pushNotificationOptionReader.isSendableTime()
                && pushNotificationOptionReader.isAllowFeature(notificationRequest.getNotificationFeature())) {

            Set<PushNotificationType> allowedTypes = pushNotificationOptionReader.getAllowedTypes();

            pushNotificationManager.send(notificationRequest, allowedTypes);
            pushNotificationLogger.log();
        }
    }

    private void authenticationNotification(PushNotificationRequest notificationRequest) {
        pushNotificationManager.sendAuthMail(notificationRequest);
        pushNotificationLogger.log();
    }

    public void getNotificationHistory() {

    }
}
