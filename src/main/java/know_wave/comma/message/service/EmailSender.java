package know_wave.comma.message.service;

import know_wave.comma.account.service.normal.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender implements Sender {

    private final MailProperties properties;
    private final JavaMailSender mailSender;

    @Override
    public void send(String destMail, String title, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(properties.getUsername());
        mail.setTo(destMail);
        mail.setSubject(title);
        mail.setText(message);

        mailSender.send(mail);
    }

}
