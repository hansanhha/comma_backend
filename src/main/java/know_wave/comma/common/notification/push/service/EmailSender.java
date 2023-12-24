package know_wave.comma.common.notification.push.service;

import jakarta.mail.internet.MimeMessage;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setFrom(properties.getUsername());
            helper.setTo(dest);
            helper.setSubject(title);
            helper.setText(content, true);
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }

    @Override
    public boolean isSupport(PushNotificationType type) {
        return NOTIFICATION_EMAIL_TYPE == type;
    }

}
