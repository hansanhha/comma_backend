package com.know_wave.comma.comma_backend.account.repository;

import com.know_wave.comma.comma_backend.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
