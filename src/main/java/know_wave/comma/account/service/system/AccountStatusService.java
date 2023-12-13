package know_wave.comma.account.service.system;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.AccountStatus;
import know_wave.comma.account.exception.AccountStatusException;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountStatusService {

    private final AccountQueryService accountQueryService;
    private final AccountRepository accountRepository;

    public void checkAccountStatus() throws AccountStatusException {
        AccountStatus accountStatus = getAccountStatus();

        if (accountStatus == AccountStatus.BLOCKED) {
            throw new AccountStatusException(ExceptionMessageSource.BLOCKED_ACCOUNT);
        } else if (accountStatus == AccountStatus.DELETED) {
            throw new AccountStatusException(ExceptionMessageSource.DELETED_ACCOUNT);
        } else if (accountStatus == AccountStatus.INACTIVE) {
            throw new AccountStatusException(ExceptionMessageSource.INACTIVE_ACCOUNT);
        }
    }

    private AccountStatus getAccountStatus() {
        return accountRepository.findById(accountQueryService.getAuthenticatedId())
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT))
                .getAccountStatus();
    }
}
