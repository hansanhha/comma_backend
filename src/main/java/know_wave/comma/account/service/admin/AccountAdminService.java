package know_wave.comma.account.service.admin;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.auth.Role;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import know_wave.comma.common.config.security.annotation.PermissionProtection;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PermissionProtection
public class AccountAdminService {

    private final AccountRepository accountRepository;

    public AccountAdminService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void changeAccountRole(Role role, String specifiedId) {
        Account account = findAccount(specifiedId);
        account.setRole(role);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT));
    }
}
