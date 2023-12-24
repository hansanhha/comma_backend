package know_wave.comma.account.service.system;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.AccountStatus;
import know_wave.comma.account.exception.AccountAuthorityException;
import know_wave.comma.account.exception.AccountStatusException;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountCheckService {

    private final AccountQueryService accountQueryService;
    private final AccountRepository accountRepository;

    public void validateAccountStatus() throws AccountStatusException {
        AccountStatus accountStatus = getAccountStatus();

        if (accountStatus == AccountStatus.BLOCKED) {
            throw new AccountStatusException(ExceptionMessageSource.BLOCKED_ACCOUNT);
        } else if (accountStatus == AccountStatus.DELETED) {
            throw new AccountStatusException(ExceptionMessageSource.DELETED_ACCOUNT);
        } else if (accountStatus == AccountStatus.INACTIVE) {
            throw new AccountStatusException(ExceptionMessageSource.INACTIVE_ACCOUNT);
        }
    }

    public void validateOrderAuthority() {
        Role role = getRole();

        if (role.equals(Role.MEMBER_EXCLUDE_ARDUINO_ORDER) ||
                role.equals(Role.MEMBER_EXCLUDE_ARDUINO_ORDER_COMMUNITY)) {
            throw new AccountAuthorityException(ExceptionMessageSource.UNABLE_TO_ORDER);
        }
    }

    private Role getRole() {
        Account account = accountQueryService.findAccount();
        return account.getRole();
    }

    private AccountStatus getAccountStatus() {
        return accountRepository.findById(accountQueryService.getAuthenticatedId())
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT))
                .getAccountStatus();
    }
}
