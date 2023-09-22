package com.know_wave.comma.comma_backend.service;

import com.know_wave.comma.comma_backend.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.entity.AcademicStatus;
import com.know_wave.comma.comma_backend.entity.Account;
import com.know_wave.comma.comma_backend.web.dto.AccountCreateForm;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void createAccount() {
//        new AccountCreateForm("test1", "testtest1234", "test123@m365.dongyang.ac.kr", "20151010", AcademicMajor.SoftwareEngineering, AcademicStatus.Enrolled);
    }

    @Test
    public void join() {
        AccountCreateForm accountCreateForm = new AccountCreateForm("test1", "testtest1234", "testName", "test123@m365.dongyang.ac.kr", "20151010", AcademicMajor.SoftwareEngineering, AcademicStatus.Enrolled);
        String id = accountService.join(accountCreateForm);
        em.flush();
        em.clear();
        Account findAccount = accountService.getOne(id);

        System.out.println(findAccount.getId());
        assertThat(findAccount.getId()).isEqualTo("test1");
    }
}