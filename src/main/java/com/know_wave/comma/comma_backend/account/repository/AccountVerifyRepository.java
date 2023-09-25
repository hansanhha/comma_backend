package com.know_wave.comma.comma_backend.account.repository;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.AccountEmailVerify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountVerifyRepository extends JpaRepository<AccountEmailVerify, String> {

}
