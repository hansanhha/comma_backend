package com.know_wave.comma.comma_backend.repository.account;

import com.know_wave.comma.comma_backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
