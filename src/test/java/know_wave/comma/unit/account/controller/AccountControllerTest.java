package know_wave.comma.unit.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Encoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import know_wave.comma.account.controller.AccountController;
import know_wave.comma.account.dto.AccountCreateForm;
import know_wave.comma.account.dto.EmailSendRequest;
import know_wave.comma.account.dto.EmailVerifyRequest;
import know_wave.comma.account.exception.NotFoundEmailException;
import know_wave.comma.account.exception.NotVerifiedException;
import know_wave.comma.account.service.AccountManagementService;
import know_wave.comma.account.service.SignUpService;
import know_wave.comma.config.security.config.SecurityConfig;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유닛 테스트(컨트롤러) : 계정(이메일 인증 및 회원가입)")
@WebMvcTest(controllers = {AccountController.class})
@Import({SecurityConfig.class})
public class AccountControllerTest {

    @MockBean
    private JwtTokenHandler jwtTokenService;

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
                .alwaysExpect(content().contentType(APPLICATION_JSON))
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
        AccountCreateForm accountCreateForm = AccountCreateForm.create(accountId, password, phone, name, email, academicNumber, major);
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
        AccountCreateForm accountCreateForm = AccountCreateForm.create(accountId, password, phone, name, email, academicNumber, major);
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
        AccountCreateForm accountCreateForm = AccountCreateForm.create(accountId, password, phone, name, email, academicNumber, major);
        String content = mapper.writeValueAsString(accountCreateForm);

        doThrow(new NotFoundEmailException("Not Verified Email")).when(signUpService).join(any());

        //when & then
        mockMvc.perform(post("/account/signup")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError());
    }

}