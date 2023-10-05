package com.know_wave.comma.comma_backend.service;

import com.know_wave.comma.comma_backend.account.service.AccountService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}