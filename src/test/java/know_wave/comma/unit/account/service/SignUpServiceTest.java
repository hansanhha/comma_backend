package know_wave.comma.unit.account.service;

import know_wave.comma.account.dto.AccountCreateForm;
import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.EmailVerify;
import know_wave.comma.account.exception.NotVerifiedException;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.account.repository.EmailVerifyRepository;
import know_wave.comma.account.service.SignUpService;
import know_wave.comma.common.notification.push.dto.AccountEmailNotificationRequest;
import know_wave.comma.common.notification.push.service.PushNotificationGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmailVerifyRepository emailVerifyRepository;

    @Mock
    private PushNotificationGateway pushNotificationGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignUpService signUpService;

    @Test
    void givenEmailVerified_whenJoin_thenSuccess() {
        //given
        AccountCreateForm form = AccountCreateForm
                .create("test", "testpassword", "01012345678", "test", "test@test.com", "2016101234", "SoftwareEngineering");
        EmailVerify emailVerify = EmailVerify.create(form.getEmail(), 123456);
        emailVerify.setVerified(true);
        Account account = Account.create(form.getAccountId(), form.getEmail(), form.getName(), form.getPassword(), form.getPhone(), form.getAcademicNumber(), AcademicMajor.valueOf(form.getMajor()));

        when(emailVerifyRepository.findById(form.getEmail())).thenReturn(Optional.of(emailVerify));

        //when
        signUpService.join(form);

        //then
        verify(emailVerifyRepository, times(1)).findById(form.getEmail());
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();
        assertThat(account).isEqualTo(savedAccount);
    }

    @Test
    void givenEmailNotVerified_whenJoin_thenNotVerifiedException() {
        //given
        AccountCreateForm form = AccountCreateForm
                .create("test", "testpassword", "01012345678", "test", "test@test.com", "2016101234", "SoftwareEngineering");
        EmailVerify emailVerify = EmailVerify.create(form.getEmail(), 123456);

        when(emailVerifyRepository.findById(form.getEmail())).thenReturn(Optional.of(emailVerify));

        //when & then
        assertThatThrownBy(() -> signUpService.join(form)).isInstanceOf(NotVerifiedException.class);
    }

    @Test
    void givenEmailNotExists_whenJoin_thenNotVerifiedException() {
        //given
        AccountCreateForm form = AccountCreateForm
                .create("test", "testpassword", "01012345678", "test", "test@test.com", "2016101234", "SoftwareEngineering");

        //when & then
        assertThatThrownBy(() -> signUpService.join(form)).isInstanceOf(NotVerifiedException.class);
    }


    @Test
    void givenFirstEmail_whenSendEmail_thenSaveEmailVerify() {
        //given
        when(emailVerifyRepository.findById(anyString())).thenReturn(Optional.empty());

        //when
        signUpService.sendEmailVerifyCode(anyString());

        //then
        verify(emailVerifyRepository, times(1)).findById(anyString());
        verify(emailVerifyRepository, times(1)).save(ArgumentMatchers.any(EmailVerify.class));
        verify(pushNotificationGateway, times(1))
                .accountEmailVerifyNotification(ArgumentMatchers.any(AccountEmailNotificationRequest.class));
    }

    @Test
    void givenEmailExisted_whenSendEmail_thenUpdateEmailVerify() {
        //given
        EmailVerify emailVerify = mock(EmailVerify.class);
        when(emailVerifyRepository.findById(anyString())).thenReturn(Optional.of(emailVerify));

        //when
        signUpService.sendEmailVerifyCode(anyString());

        //then
        verify(emailVerifyRepository, times(1)).findById(anyString());
        verify(emailVerify, times(1)).setCode(anyInt());
        verify(pushNotificationGateway, times(1))
                .accountEmailVerifyNotification(ArgumentMatchers.any(AccountEmailNotificationRequest.class));
    }

    @Test
    void givenValidCode_whenVerify_thenVerified() {
        //given
        EmailVerify mockEmailVerify = mock(EmailVerify.class);
        when(emailVerifyRepository.findById(anyString())).thenReturn(Optional.of(mockEmailVerify));
        when(mockEmailVerify.isValidCode(anyInt())).thenReturn(true);

        //when
        boolean result = signUpService.verifyEmailCode("test@test.com", 123456);

        //then
        verify(emailVerifyRepository, times(1)).findById(anyString());
        verify(mockEmailVerify, times(1)).isVerified();
        assertThat(result).isTrue();
    }

    @Test
    void givenInvalidCode_whenVerify_thenFailedVerify() {
        //given
        EmailVerify mockEmailVerify = mock(EmailVerify.class);
        when(emailVerifyRepository.findById(anyString())).thenReturn(Optional.of(mockEmailVerify));
        when(mockEmailVerify.isValidCode(anyInt())).thenReturn(false);

        //when
        boolean result = signUpService.verifyEmailCode("test@test.com", 123456);

        //then
        verify(emailVerifyRepository, times(1)).findById(anyString());
        verify(mockEmailVerify, times(1)).isVerified();
        assertThat(result).isFalse();
    }

}