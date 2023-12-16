package know_wave.comma.common.security.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
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
        Optional<Account> o = accountRepository.findById(username);

        if (o.isPresent()) {
            Account account = o.get();

            return account.toUserDetails();

        } else {
            throw new UsernameNotFoundException("NotExist Account");
        }
    }
}
