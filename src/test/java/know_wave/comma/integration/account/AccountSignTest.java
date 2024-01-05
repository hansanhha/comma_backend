package know_wave.comma.integration.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import know_wave.comma.account.dto.EmailSendRequest;
import know_wave.comma.account.dto.EmailVerifyRequest;
import know_wave.comma.account.dto.SignUpRequest;
import know_wave.comma.account.entity.EmailVerify;
import know_wave.comma.account.repository.EmailVerifyRepository;
import know_wave.comma.account.service.AccountManagementService;
import know_wave.comma.account.service.SignUpService;
import know_wave.comma.config.security.dto.SignInRequest;
import know_wave.comma.config.security.dto.SignInResponse;
import know_wave.comma.config.security.service.JwtLogoutHandler;
import know_wave.comma.config.security.service.JwtSignInHandler;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;


import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
    TODO : 독립된 테스트 구현 및 토큰 발급 관련 로직 수정 필요
 */
@DisplayName("통합 테스트 : 계정(회원가입, 로그인, 로그아웃, JWT)")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.config.location=classpath:application-test.yml"})
//@PropertySource("classpath:application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountSignTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private JwtSignInHandler signInService;

    @Autowired
    private EmailVerifyRepository emailVerifyRepository;

    private final static HttpHeaders header = new HttpHeaders();

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String MESSAGE = "msg";
    private static final String DATA = "body";
    private static final String BEARER = "Bearer ";

    private static final String testEmail = "test9999@m365.dongyang.ac.kr";
    private static final String username = "test-user";
    private static final String password = "test-password";

    @BeforeAll
    static void setUp() {
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        header.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @AfterEach
    void tearDown() {
//        jdbcTemplate.update("DELETE FROM account WHERE email LIKE 'test-user%'");
    }

    /*
        참고사항 : 이메일 전송 옵션
            1. 기존처럼 외부 시스템 사용
            2. 테스트 Mock 이메일 구현체 사용(직접 구현 필요)
            3. Spy 사용
     */
    @DisplayName("학생 이메일 인증 코드 전송 성공(외부 시스템 사용)")
    @Test
    @Order(1)
    void sendVerifyCode() throws Exception {
        //given
        EmailSendRequest emailSendRequest = EmailSendRequest.create(testEmail);
        String body = mapper.writeValueAsString(emailSendRequest);

        //when
        ResponseEntity<Map> response = restTemplate.exchange(url() + "/email/verify/request", HttpMethod.POST,
                new HttpEntity<>(body, header), Map.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(MESSAGE)).isEqualTo("sent email code");
    }

    @DisplayName("학생 이메일 인증 성공")
    @Test
    @Order(2)
    void verifyEmail() throws Exception {
        //given
        Optional<EmailVerify> optionalEmailVerify = emailVerifyRepository.findById(testEmail);
        EmailVerify emailVerify = optionalEmailVerify.get();
        EmailVerifyRequest emailVerifyRequest = EmailVerifyRequest.create(testEmail, String.valueOf(emailVerify.getCode()));
        String body = mapper.writeValueAsString(emailVerifyRequest);

        //when
        ResponseEntity<Map> response = restTemplate.exchange(url() + "/email/verify", HttpMethod.POST,
                new HttpEntity<>(body, header), Map.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(MESSAGE)).isEqualTo("authenticated email");
    }

    @DisplayName("회원가입 성공")
    @Test
    @Order(3)
    void signUp() throws Exception {
        //given
        SignUpRequest signUpRequest = SignUpRequest.create(username, password,
                "01012345678", "name", testEmail,
                "20181818", "SoftwareEngineering");

        String body = mapper.writeValueAsString(signUpRequest);

        //when
        ResponseEntity<Map> response = restTemplate.exchange(url() + "/signup", HttpMethod.POST,
                new HttpEntity<>(body, header), Map.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().get(MESSAGE)).isEqualTo("Created account");

    }

    @DisplayName("로그인 성공")
    @Test
    @Order(4)
    void signIn() throws Exception {
        //given
        SignInRequest signInRequest = SignInRequest.create(username, password);
        String body = mapper.writeValueAsString(signInRequest);

        //when
        ResponseEntity<Map> response = restTemplate.exchange(url() + "/signin", HttpMethod.POST,
                new HttpEntity<>(body, header), Map.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(MESSAGE)).isEqualTo("authenticated");
        assertThat(response.getBody().get(DATA)).isNotNull();

        String testTokenDeleteQuery = "DELETE FROM token WHERE account_id = ?";
        jdbcTemplate.update(testTokenDeleteQuery, username);
    }

    @DisplayName("JWT 재발급 성공")
    @Test
    @Order(5)
    void jwt() throws Exception {
        //given
        SignInRequest signInRequest = SignInRequest.create(username, password);
        String body = mapper.writeValueAsString(signInRequest);

        ResponseEntity<Map> signInResponseEntity = restTemplate.exchange(url() + "/signin", HttpMethod.POST,
                new HttpEntity<>(body, header), ParameterizedTypeReference.forType(Map.class));

        Map signInResponse = (Map) signInResponseEntity.getBody().get(DATA);

        String refreshToken = signInResponse.get("refresh_token").toString();

        header.add(HttpHeaders.AUTHORIZATION, BEARER + refreshToken);

        //when
        ResponseEntity<Map> response = restTemplate.exchange(url() + "/refresh-token", HttpMethod.GET,
                new HttpEntity<>(header), Map.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(MESSAGE)).isEqualTo("reissued access token");
        assertThat(response.getBody().get(DATA)).isNotNull();

        String testTokenDeleteQuery = "DELETE FROM token WHERE account_id = ?";
        jdbcTemplate.update(testTokenDeleteQuery, username);
    }

    @DisplayName("로그아웃 성공")
    @Test
    @Order(6)
    void signOut() throws Exception {
        //given
        SignInRequest signInRequest = SignInRequest.create(username, password);
        String body = mapper.writeValueAsString(signInRequest);

        ResponseEntity<Map> signInResponseEntity = restTemplate.exchange(url() + "/signin", HttpMethod.POST,
                new HttpEntity<>(body, header), ParameterizedTypeReference.forType(Map.class));

        Map signInResponse = (Map) signInResponseEntity.getBody().get(DATA);

        String accessToken = signInResponse.get("access_token").toString();

        header.add(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        //when
        ResponseEntity<Map> response = restTemplate.exchange(url() + "/signout", HttpMethod.GET,
                new HttpEntity<>(header), Map.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void clear() {
        String testAccountDeleteQuery = "DELETE FROM account WHERE account_id = ?";
        String testEmailDeleteQuery = "DELETE FROM email_verify WHERE account_verify_email = ?";
        String testTokenDeleteQuery = "DELETE FROM token WHERE account_id = ?";

        jdbcTemplate.update(testEmailDeleteQuery, testEmail);
        jdbcTemplate.update(testTokenDeleteQuery, username);
        jdbcTemplate.update(testAccountDeleteQuery, username);
    }


    private String url() {
        return "http://localhost:" + port + "/account";
    }

}
