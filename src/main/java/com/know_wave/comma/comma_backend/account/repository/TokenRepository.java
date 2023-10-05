package com.know_wave.comma.comma_backend.account.repository;

import com.know_wave.comma.comma_backend.account.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "select t from Token t join Account a on t.account.id = a.id" +
            " where a.id = :id and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidByAccount(String id);

    Token findByToken(String token);
}
