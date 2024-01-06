package know_wave.comma.unit.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import know_wave.comma.account.controller.AccountController;
import know_wave.comma.account.dto.EmailSendRequest;
import know_wave.comma.account.dto.EmailVerifyRequest;
import know_wave.comma.account.dto.SignUpRequest;
import know_wave.comma.account.exception.NotFoundAccountException;
import know_wave.comma.account.exception.NotFoundEmailException;
import know_wave.comma.account.exception.NotVerifiedException;
import know_wave.comma.account.service.AccountManagementService;
import know_wave.comma.account.service.SignUpService;
import know_wave.comma.config.security.config.SecurityConfig;
import know_wave.comma.config.security.dto.SignInRequest;
import know_wave.comma.config.security.dto.SignInResponse;
import know_wave.comma.config.security.exception.SignInFailureException;
import know_wave.comma.config.security.filter.JwtAuthenticationFilter;
import know_wave.comma.config.security.filter.LoggingFilter;
import know_wave.comma.config.security.service.JwtLogoutHandler;
import know_wave.comma.config.security.service.JwtSignInHandler;
import know_wave.comma.config.security.service.JwtTokenHandler;
import know_wave.comma.config.security.service.ResponseAccessDeniedHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유닛 테스트(컨트롤러) : 계정(이메일 인증, 회원가입, JWT)")
@WebMvcTest(controllers = {AccountController.class}, properties = {"spring.config.location=classpath:application-test.yml"})
@Import({SecurityConfig.class})
public class AccountControllerTest {

    @MockBean
    private JwtTokenHandler jwtTokenHandler;

    @MockBean
    private JwtSignInHandler signInService;

    @MockBean
    private JwtLogoutHandler logoutService;

    @MockBean
    private AccountManagementService accountManagementService;

    @MockBean
    private SignUpService signUpService;

    @MockBean
    private ResponseAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private LoggingFilter loggingFilter;

    @Autowired
    private WebApplicationContext context;

    @Value("${TOKEN_SECRET_KEY}")
    private String key;

    private Key secrectKey;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private String accessToken;
    private String refreshToken;
    public static final String BEARER = "Bearer ";

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }


    @DisplayName("학생 이메일 인증 코드 요청 성공(올바른 이메일 형식)")
    @Test
    void givenValidEmail_whenEmailVerify_thenSendEmail() throws Exception {
        //given
        String email = "test123@m365.dongyang.ac.kr";
        EmailSendRequest emailSendRequest = EmailSendRequest.create(email);
        String body = mapper.writeValueAsString(emailSendRequest);

        doNothing().when(signUpService).sendEmailVerifyCode(email);

        //when & then
        mockMvc.perform(post("/account/email/verify/request")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @DisplayName("학생 이메일 인증 코드 요청 실패(이메일 형식 오류)")
    @Test
    void givenInvalidEmail_whenEmailVerify_thenBadRequest() throws Exception {
        //given
        String email = "test123@invalid.com";
        EmailSendRequest emailSendRequest = EmailSendRequest.create(email);
        String content = mapper.writeValueAsString(emailSendRequest);

        doNothing().when(signUpService).sendEmailVerifyCode(email);

        //when & then
        mockMvc.perform(post("/account/email/verify/request")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("학생 이메일 인증 코드 검증 성공(올바른 코드)")
    @Test
    void givenValidEmailVerifyCode_whenVerifyEmail_thenOk() throws Exception {
        //given
        String email = "test@m365.dongyang.ac.kr";
        String code = "123456";
        EmailVerifyRequest emailVerifyRequest = EmailVerifyRequest.create(email, code);
        String content = mapper.writeValueAsString(emailVerifyRequest);

        when(signUpService.verifyEmailCode(email, Integer.parseInt(code))).thenReturn(true);

        //when & then
        mockMvc.perform(post("/account/email/verify")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @DisplayName("학생 이메일 인증 코드 검증 실패(틀린 코드)")
    @Test
    void givenInValidEmailVerifyCode_whenVerifyEmail_thenBadRequest() throws Exception {
        //given
        String email = "test@m365.dongyang.ac.kr";
        String code = "999999";
        EmailVerifyRequest emailVerifyRequest = EmailVerifyRequest.create(email, code);
        String content = mapper.writeValueAsString(emailVerifyRequest);

        when(signUpService.verifyEmailCode(email, Integer.parseInt(code))).thenReturn(false);

        //when & then
        mockMvc.perform(post("/account/email/verify")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 성공")
    @Test
    void givenValidSignUpRequest_whenSignUp_thenCreated() throws Exception {
        //given
        String accountId = "test123";
        String password = "testpassword";
        String email = "test@m365.dongyang.ac.kr";
        String name = "test";
        String phone = "01012345678";
        String academicNumber = "20151234";
        String major = "SoftwareEngineering";
        SignUpRequest accountCreateForm = SignUpRequest.create(accountId, password, phone, name, email, academicNumber, major);
        String content = mapper.writeValueAsString(accountCreateForm);

        doNothing().when(signUpService).join(accountCreateForm);

        //when & then
        mockMvc.perform(post("/account/signup")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());
    }

    @DisplayName("회원가입 실패(미인증 학생 이메일)")
    @Test
    void givenNotVerifiedEmail_whenSignUp_thenNotVerifiedException() throws Exception {
        //given
        String accountId = "test123";
        String password = "testpassword";
        String email = "test@m365.dongyang.ac.kr";
        String name = "test";
        String phone = "01012345678";
        String academicNumber = "20151234";
        String major = "SoftwareEngineering";
        SignUpRequest accountCreateForm = SignUpRequest.create(accountId, password, phone, name, email, academicNumber, major);
        String content = mapper.writeValueAsString(accountCreateForm);

        doThrow(new NotVerifiedException("Not Verified Email")).when(signUpService).join(any());

        //when & then
        mockMvc.perform(post("/account/signup")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("회원가입 실패(찾을 수 없는 이메일)")
    @Test
    void givenNotExistEmail_whenSignUp_thenNotVerifiedException() throws Exception {
        //given
        String accountId = "test123";
        String password = "testpassword";
        String email = "test@m365.dongyang.ac.kr";
        String name = "test";
        String phone = "01012345678";
        String academicNumber = "20151234";
        String major = "SoftwareEngineering";
        SignUpRequest accountCreateForm = SignUpRequest.create(accountId, password, phone, name, email, academicNumber, major);
        String content = mapper.writeValueAsString(accountCreateForm);

        doThrow(new NotFoundEmailException("Not Verified Email")).when(signUpService).join(any());

        //when & then
        mockMvc.perform(post("/account/signup")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("로그인 성공")
    @Test
    void givenValidSignInRequest_whenSignIn_thenOk() throws Exception {
        //given
        String accountId = "test123";
        String password = "testpassword";
        SignInRequest signInRequest = SignInRequest.create(accountId, password);
        String content = mapper.writeValueAsString(signInRequest);

        when(signInService.signIn(accountId, password)).thenReturn(mock(SignInResponse.class));

        //when & then
        mockMvc.perform(post("/account/signin")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk());
    }

    @DisplayName("로그인 실패(계정 없음)")
    @Test
    void givenNotExistAccount_whenSignIn_thenNotFound() throws Exception {
        //given
        String accountId = "test123";
        String password = "testpassword";
        SignInRequest signInRequest = SignInRequest.create(accountId, password);
        String content = mapper.writeValueAsString(signInRequest);

        when(signInService.signIn(accountId, password)).thenThrow(new NotFoundAccountException("계정이 존재하지 않습니다"));

        //when & then
        mockMvc.perform(post("/account/signin")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("로그인 실패(비밀번호 틀림)")
    @Test
    void givenInvalidPassword_whenSignIn_thenNotFound() throws Exception {
        //given
        String accountId = "test123";
        String password = "testpassword";
        SignInRequest signInRequest = SignInRequest.create(accountId, password);
        String content = mapper.writeValueAsString(signInRequest);

        when(signInService.signIn(accountId, password)).thenThrow(new SignInFailureException("비밀번호가 틀렸습니다"));

        //when & then
        mockMvc.perform(post("/account/signin")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("로그아웃 성공")
    @Test
    void signout() throws Exception {
        //given
        String subject = "test-user";
        Date issuedAt = new Date();
        Date expired = new Date(issuedAt.getTime() + 100000);

        byte[] decoded = Decoders.BASE64.decode(key);
        secrectKey = Keys.hmacShaKeyFor(decoded);

        String jjwt = Jwts.builder()
                .setSubject(subject)
                .setClaims(Map.of("ROLE", "ROLE_USER"))
                .setIssuedAt(issuedAt)
                .setExpiration(expired)
                .signWith(secrectKey)
                .compact();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secrectKey)
                .build()
                .parseClaimsJws(jjwt)
                .getBody();

        when(jwtTokenHandler.getPayload(anyString())).thenReturn(claims);
        doNothing().when(logoutService).logout(any(), any(), any());

        //when & then
        mockMvc.perform(post("/account/signout")
                        .header(HttpHeaders.AUTHORIZATION, BEARER + jjwt))
                .andExpect(status().isOk());
    }

}