package know_wave.comma.account.config;

import know_wave.comma.account.exception.AccountStatusException;
import know_wave.comma.account.service.system.AccountStatusService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckAccountStatusAspect {

    private final AccountStatusService accountStatusService;

    @Before("@annotation(know_wave.comma.account.config.CheckAccountStatus)")
    public void checkAccountStatus() throws AccountStatusException {
        accountStatusService.checkAccountStatus();
    }
}
