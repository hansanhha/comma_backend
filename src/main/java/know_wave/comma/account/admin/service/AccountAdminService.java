package know_wave.comma.account.admin.service;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.admin.dto.PermissionChangeRequest;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.entity.Authority;
import know_wave.comma.config.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountAdminService {

    private final AccountRepository accountRepository;

    public void upgradeRole(PermissionChangeRequest changeRequest) {
        String role = changeRequest.getRole().toLowerCase();

        switch (role) {
            case "manager" -> appointManager(changeRequest.getAccountId());
            case "arduino_order" -> grantPermission(Authority.MEMBER_ARDUINO_ORDER, changeRequest.getAccountId());
            case "community" -> grantPermission(Authority.MEMBER_CREATE, changeRequest.getAccountId());
        }
    }

    public void downgradeRole(PermissionChangeRequest changeRequest) {
        String role = changeRequest.getRole().toLowerCase();

        switch (role) {
            case "manager" -> unAppointManager(changeRequest.getAccountId());
            case "arduino_order" -> removePermission(Authority.MEMBER_ARDUINO_ORDER, changeRequest.getAccountId());
            case "community" -> removePermission(Authority.MEMBER_CREATE, changeRequest.getAccountId());
        }
    }

    private void appointManager(String accountId) {
        Account account = findAccount(accountId);
        account.setRole(Role.MANAGER);
    }

    private void unAppointManager(String accountId) {
        Account account = findAccount(accountId);
        account.setRole(Role.MEMBER);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT));
    }

    private void grantPermission(Authority authority, String accountId) {
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

    private void removePermission(Authority authority, String accountId) {
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
