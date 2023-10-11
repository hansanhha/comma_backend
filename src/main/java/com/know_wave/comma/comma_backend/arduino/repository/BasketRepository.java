package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    Optional<Basket> findByAccountAndArduino(Account account, Arduino arduino);

    List<Basket> findAllByAccount(Account account);

    @Query("select b " +
            "from Basket b " +
            "join fetch b.arduino ba " +
            "join fetch ba.categories bac " +
            "join fetch bac.category " +
            "where b.account = :account")
    List<Basket> findAllFetchArduinoByAccount(Account account);

}
