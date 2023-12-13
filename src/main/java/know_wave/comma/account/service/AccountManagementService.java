package know_wave.comma.account.service;

import know_wave.comma.account.dto.AccountResponse;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.account.service.system.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountManagementService {

    private final AccountQueryService accountQueryService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountResponse getAccount() {
        Account account = accountQueryService.findAccount();

        return AccountResponse.of(account);
    }

    public boolean checkMatchPassword(String password) {
        Account account = accountQueryService.findAccount();

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(String password) {
        Account account = accountQueryService.findAccount();

        account.setPassword(passwordEncoder.encode(password));
    }


    public void deleteAccount() {
        Account account = accountQueryService.findAccount();

        accountRepository.delete(account);
    }


}
