package know_wave.comma.account.aop;

import know_wave.comma.account.exception.AccountStatusException;
import know_wave.comma.account.service.system.AccountCheckService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckAccountStatusAspect {

    private final AccountCheckService accountStatusService;

    @Before("@annotation(know_wave.comma.account.aop.CheckAccountStatus)")
    public void checkAccountStatus() throws AccountStatusException {
        accountStatusService.validateAccountStatus();
    }
}
