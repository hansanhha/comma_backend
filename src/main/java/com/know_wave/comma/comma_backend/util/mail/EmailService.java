package com.know_wave.comma.comma_backend.util.mail;

import com.know_wave.comma.comma_backend.account.entity.AccountEmailVerify;
import com.know_wave.comma.comma_backend.account.repository.AccountVerifyRepository;
import com.know_wave.comma.comma_backend.util.mail.exception.EmailVerifiedException;
import com.know_wave.comma.comma_backend.util.mail.exception.NotFoundEmailException;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
public class EmailService {

    private final AccountVerifyRepository accountVerifyRepository;
    private final EmailSender emailSender;
    public static final String AUTH_CODE_TITLE = "컴마 인증 코드";

    public EmailService(AccountVerifyRepository accountVerifyRepository, EmailSender emailSender) {
        this.accountVerifyRepository = accountVerifyRepository;
        this.emailSender = emailSender;
    }

    public void send(String receiveEmail, String title, String text) {
        if (receiveEmail.isEmpty() || title.isEmpty() || text.isEmpty()) {
            throw new IllegalArgumentException(UNABLE_SEND_EMAIL);
        }
        emailSender.send(receiveEmail, title, text);
    }

    public void sendAuthCode(String email) {

        final int code = GenerateUtils.generateSixRandomCode();

        accountVerifyRepository.findById(email).ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(ALREADY_VERIFIED_EMAIL);
                    }

                    accountEmailVerify.setCode(code);
                    emailSender.send(email, AUTH_CODE_TITLE, String.valueOf(code));
                },
                () -> {
                    AccountEmailVerify emailVerify = new AccountEmailVerify(email, false, code);
                    accountVerifyRepository.save(emailVerify);
                    emailSender.send(email, AUTH_CODE_TITLE, String.valueOf(code));
                });
    }

    public boolean verifyAuthCode(String email, int code) {
        // 배열 자체에 대한 참조는 변하지 않으므로 클로저 가능 (final)
        final boolean[] result = {false};

        accountVerifyRepository.findById(email).ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(ALREADY_VERIFIED_EMAIL);
                    }

                    if (accountEmailVerify.verifyCode(code)) {
                        accountEmailVerify.setVerified(true);
                        result[0] = true;
                    }
                },
                ()-> {
                    throw new NotFoundEmailException(NOT_FOUND_EMAIL);
                });

        return result[0];
    }
}
