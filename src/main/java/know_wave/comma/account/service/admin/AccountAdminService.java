package know_wave.comma.account.service.admin;

import know_wave.comma.account.entity.Account;
import know_wave.comma.config.security.entity.Role;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.security.annotation.AdminPermissionProtection;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AdminPermissionProtection
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
