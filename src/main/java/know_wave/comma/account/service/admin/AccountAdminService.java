package know_wave.comma.account.service.admin;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.entity.Authority;
import know_wave.comma.config.security.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountAdminService {

    private final AccountRepository accountRepository;

    public AccountAdminService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void appointManager(String accountId) {
        Account account = findAccount(accountId);
        account.setRole(Role.MANAGER);
    }

    public void unAppointManager(String accountId) {
        Account account = findAccount(accountId);
        account.setRole(Role.MEMBER);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT));
    }

    public void grantPermission(Authority authority, String accountId) {
        Account account = findAccount(accountId);

        switch (authority) {
            case MEMBER_CREATE -> {
                if (account.getRole() == Role.MEMBER_EXCLUDE_ARDUINO_ORDER_COMMUNITY) {
                    account.setRole(Role.MEMBER_EXCLUDE_ARDUINO_ORDER);
                } else {
                    account.setRole(Role.MEMBER);
                }
            }
            case MEMBER_ARDUINO_ORDER -> {
                if (account.getRole() == Role.MEMBER_EXCLUDE_ARDUINO_ORDER_COMMUNITY) {
                    account.setRole(Role.MEMBER_EXCLUDE_COMMUNITY);
                } else {
                    account.setRole(Role.MEMBER);
                }
            }
        }
    }

    public void removePermission(Authority authority, String accountId) {
        Account account = findAccount(accountId);

        switch (authority) {
            case MEMBER_CREATE -> {
                if (account.getRole() == Role.MEMBER_EXCLUDE_ARDUINO_ORDER) {
                    account.setRole(Role.MEMBER_EXCLUDE_ARDUINO_ORDER_COMMUNITY);
                } else {
                    account.setRole(Role.MEMBER_EXCLUDE_COMMUNITY);
                }
            }
            case MEMBER_ARDUINO_ORDER -> {
                if (account.getRole() == Role.MEMBER_EXCLUDE_COMMUNITY) {
                    account.setRole(Role.MEMBER_EXCLUDE_ARDUINO_ORDER_COMMUNITY);
                } else {
                    account.setRole(Role.MEMBER_EXCLUDE_ARDUINO_ORDER);
                }
            }
        }
    }
}
