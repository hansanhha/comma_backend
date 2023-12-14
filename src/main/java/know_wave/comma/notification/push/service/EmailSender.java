package know_wave.comma.notification.push.service;

import know_wave.comma.notification.push.entity.PushNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
class EmailSender implements PushNotificationSender {

    private static final PushNotificationType NOTIFICATION_EMAIL_TYPE = PushNotificationType.EMAIL;
    private final MailProperties properties;
    private final JavaMailSender mailSender;

    @Override
    public void send(String dest, String title, String content, Map<Object, Object> paramMap) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(properties.getUsername());
        mail.setTo(dest);
        mail.setSubject(title);
        mail.setText(content);

        mailSender.send(mail);
    }

    @Override
    public boolean isSupport(PushNotificationType type) {
        return NOTIFICATION_EMAIL_TYPE == type;
    }

}
