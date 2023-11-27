package know_wave.comma.common.config.security.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

//@Service
public class AccountAuthenticationProvider implements AuthenticationProvider {

    private final AccountUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AccountAuthenticationProvider(AccountUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String rawPassword = String.valueOf(authentication.getCredentials());
        UserDetails user = userDetailsService.loadUserByUsername(username);

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());

        if (matches) {
            return UsernamePasswordAuthenticationToken.authenticated(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        } else {
            throw new BadCredentialsException("아이디 또는 비밀번호가 맞지 않습니다");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
