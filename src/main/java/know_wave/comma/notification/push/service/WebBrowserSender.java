package know_wave.comma.notification.push.service;

import know_wave.comma.notification.push.entity.PushNotificationType;

import java.util.Map;

class WebBrowserSender implements PushNotificationSender {
    private static final PushNotificationType NOTIFICATION_WEB_TYPE = PushNotificationType.WEB;

    @Override
    public void send(String dest, String title, String content, Map<Object, Object> paramMap) {

    }

    @Override
    public boolean isSupport(PushNotificationType type) {
        return NOTIFICATION_WEB_TYPE == type;
    }
}
