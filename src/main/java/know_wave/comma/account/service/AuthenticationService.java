package know_wave.comma.account.service;

import know_wave.comma.account.entity.AccountEmailVerify;
import know_wave.comma.account.repository.AccountVerifyRepository;
import know_wave.comma.notification.alarm.exception.EmailVerifiedException;
import know_wave.comma.notification.alarm.exception.NotFoundEmailException;
import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import know_wave.comma.notification.alarm.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountVerifyRepository accountVerifyRepository;
    private final EmailSender emailSender;

    public static final String AUTH_CODE_TITLE = "컴마 인증 코드";

    public void sendAuthCode(String email) {

        final int code = generateSixRandomCode();

        accountVerifyRepository.findById(email).ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(ExceptionMessageSource.ALREADY_VERIFIED_EMAIL);
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
                        throw new EmailVerifiedException(ExceptionMessageSource.ALREADY_VERIFIED_EMAIL);
                    }

                    if (accountEmailVerify.verifyCode(code)) {
                        accountEmailVerify.setVerified(true);
                        result[0] = true;
                    }
                },
                ()-> {
                    throw new NotFoundEmailException(ExceptionMessageSource.NOT_FOUND_EMAIL);
                });

        return result[0];
    }

    private int generateSixRandomCode() {
        return ThreadLocalRandom.current().nextInt(888888) + 111111;
    }
}
