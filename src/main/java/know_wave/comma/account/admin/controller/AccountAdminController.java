package know_wave.comma.account.admin.controller;

import know_wave.comma.account.admin.service.AccountAdminService;
import know_wave.comma.account.admin.dto.PermissionChangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AccountAdminController {

    private final AccountAdminService accountAdminService;
    private static final String MESSAGE = "msg";
    private static final String DATA = "body";

    @PatchMapping("/permission")
    public Map<String, String> grantRole(PermissionChangeRequest changeRequest) {
        accountAdminService.upgradeRole(changeRequest);

        return Map.of(MESSAGE, "granted permission, account : " + changeRequest.getAccountId());
    }

    @DeleteMapping("/permission")
    public Map<String, String> removeRole(PermissionChangeRequest changeRequest) {
        accountAdminService.downgradeRole(changeRequest);

        return Map.of(MESSAGE, "removed permission, account : " + changeRequest.getAccountId());
    }

}

