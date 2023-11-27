package know_wave.comma.account.service.normal;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.common.message.ExceptionMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountQueryService {

    private final AccountRepository accountRepository;

    public AccountQueryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT));
    }

    public static String getAuthenticatedId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name.isEmpty() || name.equals("anonymousUser")) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_AUTHENTICATED_REQUEST);
        }
        return name;
    }
}
