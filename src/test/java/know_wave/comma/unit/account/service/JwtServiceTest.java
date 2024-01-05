package know_wave.comma.unit.account.service;

import io.jsonwebtoken.Claims;
import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.config.security.dto.SignInResponse;
import know_wave.comma.config.security.entity.SecurityAccount;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.config.security.exception.*;
import know_wave.comma.config.security.service.AccountUserDetailsService;
import know_wave.comma.config.security.service.JwtSignInHandler;
import know_wave.comma.config.security.service.JwtTokenHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;



@DisplayName("유닛 테스트(서비스) : 계정(로그인 및 JWT 토큰)")
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountUserDetailsService userDetailsService;

    @Mock
    private JwtTokenHandler jwtTokenHandler;

    @InjectMocks
    private JwtSignInHandler jwtSignInHandler;

    private String username;

    private String password;

    private Account account;

    @BeforeEach
    void setUp() {
        username = "test-user";
        password = "test-password";
        account = Account.create("test-user", "test@m365.dongyang.ac.kr", "test-name",
                password, "01012345678", "20202020", AcademicMajor.AIEngineering);
    }



    @DisplayName("로그인 성공")
    @Test
    void givenAccount_whenSignIn_thenSuccess() {
        //given
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(SecurityAccount.to(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenHandler.issueAccessToken(anyString())).thenReturn("access-token");
        when(jwtTokenHandler.issueRefreshToken(anyString())).thenReturn("refresh-token");

        //when
        SignInResponse signInResponse = jwtSignInHandler.signIn(username, password);

        //then
        assertThat(signInResponse).isNotNull();
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtTokenHandler, times(1)).issueAccessToken(anyString());
        verify(jwtTokenHandler, times(1)).issueRefreshToken(anyString());
    }

    @DisplayName("로그인 실패(비밀번호 불일치)")
    @Test
    void givenInvalidPassword_whenSignIn_thenFailure() {
        //given
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(SecurityAccount.to(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> jwtSignInHandler.signIn(username, password))
                .isInstanceOf(SignInFailureException.class);
    }

    @DisplayName("로그인 실패(계정 없음)")
    @Test
    void givenInvalidAccount_whenSignIn_thenFailure() {
        //given
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(UsernameNotFoundException.class);

        //when & then
        assertThatThrownBy(() -> jwtSignInHandler.signIn(username, password))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @DisplayName("액세스 토큰 재발급 성공")
    @Test
    void givenRefreshToken_whenReissueAccessToken_thenSuccess() {
        //given
        when(jwtTokenHandler.getPayload(anyString())).thenReturn(mock(Claims.class));
        when(jwtTokenHandler.getToken(anyString())).thenReturn(Optional.of(Mockito.mock(Token.class)));
        when(jwtTokenHandler.isValidRefreshToken(anyString())).thenReturn(true);
        when(jwtTokenHandler.issueAccessToken(null)).thenReturn("access-token");

        //when
        String accessToken = jwtSignInHandler.reissueAccessToken("refresh-token");

        //then
        assertThat(accessToken).isNotNull();
        verify(jwtTokenHandler, times(1)).getPayload(anyString());
        verify(jwtTokenHandler, times(1)).getToken(anyString());
        verify(jwtTokenHandler, times(1)).isValidRefreshToken(anyString());
        verify(jwtTokenHandler, times(1)).issueAccessToken(null);
    }

    @DisplayName("액세스 토큰 재발급 실패(리프레쉬 토큰 기한 만료)")
    @Test
    void givenExpiredRefreshToken_whenReissueAccessToken_thenFailure() {
        //given
        when(jwtTokenHandler.getPayload(anyString())).thenThrow(TokenExpiredException.class);

        //when & then
        assertThatThrownBy(() -> jwtSignInHandler.reissueAccessToken("refresh-token"))
                .isInstanceOf(TokenExpiredException.class);
    }

    @DisplayName("액세스 토큰 재발급 실패(서명 실패)")
    @Test
    void givenTemperedRefreshToken_whenReissueAccessToken_thenFailure() {
        //given
        when(jwtTokenHandler.getPayload(anyString())).thenThrow(TokenTemperedException.class);

        //when & then
        assertThatThrownBy(() -> jwtSignInHandler.reissueAccessToken("refresh-token"))
                .isInstanceOf(TokenTemperedException.class);
    }

    @DisplayName("액세스 토큰 재발급 실패(없는 토큰)")
    @Test
    void givenNotExistRefreshToken_whenReissueAccessToken_thenFailure() {
        //given
        when(jwtTokenHandler.getPayload(anyString())).thenReturn(mock(Claims.class));
        when(jwtTokenHandler.getToken(anyString())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> jwtSignInHandler.reissueAccessToken("refresh-token"))
                .isInstanceOf(NotFoundTokenException.class);
    }

    @DisplayName("액세스 토큰 재발급 실패(유효하지 않은 토큰)")
    @Test
    void givenInvalidRefreshToken_whenReissueAccessToken_thenFailure() {
        //given
        when(jwtTokenHandler.getPayload(anyString())).thenReturn(mock(Claims.class));
        when(jwtTokenHandler.getToken(anyString())).thenReturn(Optional.of(Mockito.mock(Token.class)));
        when(jwtTokenHandler.isValidRefreshToken(anyString())).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> jwtSignInHandler.reissueAccessToken("refresh-token"))
                .isInstanceOf(InvalidTokenException.class);
    }

}
