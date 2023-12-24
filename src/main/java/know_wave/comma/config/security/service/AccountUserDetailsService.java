package know_wave.comma.config.security.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.entity.SecurityAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(username);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            return SecurityAccount.to(account);
        } else {
            throw new UsernameNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT);
        }
    }
}
