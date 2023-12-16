package know_wave.comma.common.security.aop;

import know_wave.comma.common.entity.ExceptionMessageSource;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminProtectionAspect {

    @Before("@annotation(know_wave.comma.common.security.annotation.AdminPermissionProtection)")
    public void checkPermission() throws AccessDeniedException {
        if (!hasPermission()) {
            throw new AccessDeniedException(ExceptionMessageSource.PERMISSION_DENIED);
        }
    }

    private boolean hasPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                            || authority.getAuthority().equals("ROLE_MANAGER"));
        }

        return false;
    }
}
