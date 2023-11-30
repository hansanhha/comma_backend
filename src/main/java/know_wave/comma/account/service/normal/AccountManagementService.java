package know_wave.comma.account.service.normal;

import know_wave.comma.account.dto.AccountResponse;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountManagementService {

    private final AccountQueryService accountQueryService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountManagementService(AccountQueryService accountQueryService, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountQueryService = accountQueryService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountResponse getAccount() {
        String accountId = accountQueryService.getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        return AccountResponse.of(account);
    }

    public boolean checkMatchPassword(String password) {
        String accountId = accountQueryService.getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(String password) {
        String accountId = accountQueryService.getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        account.setPassword(passwordEncoder.encode(password));
    }


    public void deleteAccount() {
        String accountId = accountQueryService.getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        accountRepository.delete(account);
    }


}
