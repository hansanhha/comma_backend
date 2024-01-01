package know_wave.comma.unit.account.repository;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.TokenRepository;
import know_wave.comma.config.security.entity.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("유닛 테스트(리포지토리) : JWT")
@DataJpaTest
public class JwtRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TokenRepository tokenRepository;

    private Account account;

    private Token token;
    private String tokenValue;
    private Date someTime;

    @BeforeEach
    void setUp() {
        account = Account.create("test-user", "test@m365.dongyang.ac.kr", "name",
                "test-password", "01012345678", "20202020", AcademicMajor.AIEngineering);


        tokenValue = "tokenValue";
        someTime = new Date();
        token = Token.create(account, tokenValue, someTime);

        entityManager.persist(account);
        entityManager.persist(token);
    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @DisplayName("토큰 저장 성공")
    @Test
    void saveToken() {
        //given

        //when

        //then
        Token foundToken = entityManager.find(Token.class, token.getId());
        assertThat(foundToken).isEqualTo(token);
    }

    @DisplayName("특정 토큰 조회 성공")
    @Test
    void findByToken() {
        //given

        //when
        Optional<Token> foundToken = tokenRepository.findByToken(tokenValue);

        //then
        assertThat(foundToken).isPresent();
        assertThat(foundToken.get()).isEqualTo(token);
    }

    @DisplayName("유효한 토큰들 조회 성공")
    @Test
    void findAllByAccountAndRevokedFalse() {
        //given
        Token token2 = Token.create(account, "tokenValue2", someTime);
        Token token3 = Token.create(account, "tokenValue3", someTime);
        Token token4 = Token.create(account, "tokenValue4", someTime);
        Token token5 = Token.create(account, "tokenValue5", someTime);
        token4.setExpired(true);
        token5.setRevoked(true);

        tokenRepository.saveAll(List.of(token2, token3, token4, token5));

        //when
        List<Token> validTokens = tokenRepository.findAllByAccount(account);

        //then
        assertThat(validTokens).isNotEmpty();
        assertThat(validTokens).hasSize(3);
        assertThat(validTokens.stream().noneMatch(Token::isRevoked)).isTrue();
        assertThat(validTokens.stream().noneMatch(Token::isExpired)).isTrue();
    }

    @DisplayName("토큰 삭제 성공")
    @Test
    void deleteToken() {
        //given

        //when
        tokenRepository.delete(token);

        //then
        Optional<Token> foundToken = tokenRepository.findByToken(tokenValue);
        assertThat(foundToken).isEmpty();
    }

}
