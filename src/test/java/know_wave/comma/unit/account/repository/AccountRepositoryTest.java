package know_wave.comma.unit.account.repository;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.config.security.entity.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private static Account account;

    @BeforeAll
    static void setUp() {
        account = Account.create("test", "test@test.com", "test", "test", "01012345678", "2016101234", AcademicMajor.AIEngineering);
    }

    @Test
    void saveAccount() {
        //given

        //when
        accountRepository.save(account);

        //then
        Account findAccount = entityManager.find(Account.class, account.getId());
        assertThat(findAccount).isEqualTo(account);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(account);

        //when
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        //then
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get()).isEqualTo(account);
    }

    @Test
    void updateAccount() {
        //given
        entityManager.persist(account);

        String newPassword = "newPassword";
        account.setPassword(newPassword);
        account.setRole(Role.ADMIN);

        //when
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        //then
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get()).isEqualTo(account);
        assertThat(foundAccount.get().getRole()).isEqualTo(Role.ADMIN);
        assertThat(foundAccount.get().getPassword()).isEqualTo(newPassword);
    }

    @Test
    void deleteAccount() {
        //given
        entityManager.persist(account);

        //when
        accountRepository.delete(account);
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        //then
        assertThat(foundAccount).isEmpty();
    }

}
