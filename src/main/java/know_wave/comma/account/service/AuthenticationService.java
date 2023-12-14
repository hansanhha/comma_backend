package know_wave.comma.account.service;

import know_wave.comma.account.entity.AccountEmailVerify;
import know_wave.comma.account.repository.AccountVerifyRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.notification.push.dto.PushNotificationRequest;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import know_wave.comma.account.exception.EmailVerifiedException;
import know_wave.comma.account.exception.NotFoundEmailException;
import know_wave.comma.common.notification.push.service.PushNotificationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountVerifyRepository accountVerifyRepository;
    private final PushNotificationGateway notificationGateway;

    public static final String AUTH_CODE_TITLE = "컴마 인증 코드";

    public void sendAuthCode(String email) {

        final int code = generateSixRandomCode();

        PushNotificationRequest notificationRequest = PushNotificationRequest.to(Map.of(PushNotificationType.EMAIL, email), AUTH_CODE_TITLE, String.valueOf(code), NotificationFeature.ACCOUNT_AUTH_CODE, null);

        accountVerifyRepository.findById(email).ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(ExceptionMessageSource.ALREADY_VERIFIED_EMAIL);
                    }

                    accountEmailVerify.setCode(code);
                    notificationGateway.notify(notificationRequest);
                },
                () -> {
                    AccountEmailVerify emailVerify = new AccountEmailVerify(email, false, code);
                    accountVerifyRepository.save(emailVerify);
                    notificationGateway.notify(notificationRequest);
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
