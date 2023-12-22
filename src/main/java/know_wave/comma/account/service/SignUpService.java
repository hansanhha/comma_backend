package know_wave.comma.account.service;

import know_wave.comma.account.dto.AccountCreateForm;
import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.EmailVerify;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.account.repository.EmailVerifyRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.notification.push.dto.PushNotificationRequest;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import know_wave.comma.account.exception.AlreadyVerifiedException;
import know_wave.comma.account.exception.NotFoundEmailException;
import know_wave.comma.common.notification.push.service.PushNotificationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService {

    private final EmailVerifyRepository accountVerifyRepository;
    private final PushNotificationGateway notificationGateway;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public static final String AUTH_CODE_TITLE = "컴마 이메일 인증 코드";

    public void join(AccountCreateForm form) {
        Optional<EmailVerify> optionalEmailVerify = accountVerifyRepository.findById(form.getEmail());

        if (optionalEmailVerify.isEmpty()) {
            throw new AlreadyVerifiedException(ExceptionMessageSource.NOT_FOUND_EMAIL);
        }

        EmailVerify emailVerify = optionalEmailVerify.get();

        if (!emailVerify.isVerified()) {
            throw new AlreadyVerifiedException(ExceptionMessageSource.NOT_VERIFIED_EMAIL);
        }

        Account account;
        String encodedPassword = passwordEncoder.encode(form.getPassword());

        if (form.getPhone().isBlank()) {
            account = Account.createWithoutPhone(form.getAccountId(), form.getEmail(), form.getName(), encodedPassword, form.getAcademicNumber(), AcademicMajor.valueOf(form.getMajor()));
        } else {
            account = Account.create(form.getAccountId(), form.getEmail(), form.getName(), encodedPassword, form.getPhone(), form.getAcademicNumber(), AcademicMajor.valueOf(form.getMajor()));
        }

        accountRepository.save(account);
    }

    public void sendEmailVerifyCode(String email) {

        final int verifyCode = generateSixRandomCode();

        PushNotificationRequest notificationRequest =
                PushNotificationRequest.create(Map.of(PushNotificationType.EMAIL, email), AUTH_CODE_TITLE, String.valueOf(verifyCode), NotificationFeature.ACCOUNT_VERIFY_EMAIL, null);

        Optional<EmailVerify> optionalEmailVerify = accountVerifyRepository.findById(email);

        if (optionalEmailVerify.isEmpty()) {
            EmailVerify emailVerify = EmailVerify.create(email, verifyCode);
            accountVerifyRepository.save(emailVerify);
        } else {
            EmailVerify emailVerify = optionalEmailVerify.get();
            emailVerify.setCode(verifyCode);
        }
        notificationGateway.notify(notificationRequest);
    }

    public boolean verifyEmailCode(String email, int code) {
        Optional<EmailVerify> optionalEmailVerify = accountVerifyRepository.findById(email);

        if (optionalEmailVerify.isEmpty()) {
            throw new NotFoundEmailException(ExceptionMessageSource.NOT_FOUND_EMAIL);
        }

        EmailVerify emailVerify = optionalEmailVerify.get();

        if (emailVerify.isVerified()) {
            throw new AlreadyVerifiedException(ExceptionMessageSource.ALREADY_VERIFIED_EMAIL);
        }

        if (emailVerify.isValidCode(code)) {
            emailVerify.setVerified(true);
            return true;
        }
        return false;
    }

    private int generateSixRandomCode() {
        return ThreadLocalRandom.current().nextInt(888888) + 111111;
    }
}
