package know_wave.comma.account.controller;

import know_wave.comma.account.service.admin.AccountAdminService;
import know_wave.comma.config.security.entity.Authority;
import know_wave.comma.config.security.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AccountAdminController {

    private final AccountAdminService accountAdminService;
    private static final String MESSAGE = "msg";
    private static final String DATA = "body";

    @PatchMapping("/{accountId}/appoint-manager")
    public Map<String, String> appointManager(@PathVariable String accountId) {
        accountAdminService.appointManager(accountId);

        return Map.of(MESSAGE, "appointed " + Role.MANAGER.name() + ", account : " + accountId);
    }

    @PatchMapping("/{accountId}/unappoint-manager")
    public Map<String, String> unAppointManager(@PathVariable String accountId) {
        accountAdminService.unAppointManager(accountId);

        return Map.of(MESSAGE, "unappointed " + Role.MANAGER.name() + ", account : " + accountId);
    }

    @PatchMapping("/{accountId}/grant-community-permission")
    public Map<String, String> grantCommunityPermission(@PathVariable String accountId) {
        accountAdminService.grantPermission(Authority.MEMBER_CREATE, accountId);

        return Map.of(MESSAGE, "granted community permission, account : " + accountId);
    }

    @PatchMapping("/{accountId}/remove-community-permission")
    public Map<String, String> removeCommunityPermission(@PathVariable String accountId) {
        accountAdminService.removePermission(Authority.MEMBER_CREATE, accountId);

        return Map.of(MESSAGE, "removed community permission, account : " + accountId);
    }

    @PatchMapping("/{accountId}/remove-arduino-order-permission")
    public Map<String, String> removeArduinoOrderPermission(@PathVariable String accountId) {
        accountAdminService.removePermission(Authority.MEMBER_ARDUINO_ORDER, accountId);

        return Map.of(MESSAGE, "removed arduino order permission, account : " + accountId);
    }

    @PatchMapping("/{accountId}/grant-arduino-order-permission")
    public Map<String, String> grantArduinoOrderPermission(@PathVariable String accountId) {
        accountAdminService.grantPermission(Authority.MEMBER_ARDUINO_ORDER, accountId);

        return Map.of(MESSAGE, "granted arduino order permission, account : " + accountId);
    }

}

