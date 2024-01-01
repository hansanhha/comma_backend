package know_wave.comma.unit.account.repository;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.config.security.entity.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유닛 테스트(리포지토리) : 계정")
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("계정 저장 성공")
    @Test
    void saveAccount() {
        //given
        Account account = Account.create("test", "test@test.com", "test", "test", "01012345678", "2016101234", AcademicMajor.AIEngineering);

        //when
        accountRepository.save(account);

        //then
        Account findAccount = entityManager.find(Account.class, account.getId());
        assertThat(findAccount).isEqualTo(account);
    }

    @DisplayName("계정 조회 성공")
    @Test
    void findById() {
        //given
        Account account = Account.create("test", "test@test.com", "test", "test", "01012345678", "2016101234", AcademicMajor.AIEngineering);
        entityManager.persist(account);

        //when
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        //then
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get()).isEqualTo(account);
    }

    @DisplayName("계정 수정 성공")
    @Test
    void updateAccount() {
        //given
        Account account = Account.create("test", "test@test.com", "test", "test", "01012345678", "2016101234", AcademicMajor.AIEngineering);
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

    @DisplayName("계정 삭제 성공")
    @Test
    void deleteAccount() {
        //given
        Account account = Account.create("test", "test@test.com", "test", "test", "01012345678", "2016101234", AcademicMajor.AIEngineering);
        entityManager.persist(account);

        //when
        accountRepository.delete(account);
        entityManager.clear();
        Optional<Account> foundAccount = accountRepository.findById(account.getId());

        //then
        assertThat(foundAccount).isEmpty();
    }

}
