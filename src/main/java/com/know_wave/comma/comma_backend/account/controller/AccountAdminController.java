package com.know_wave.comma.comma_backend.account.controller;

import com.know_wave.comma.comma_backend.account.dto.AccountIdRequest;
import com.know_wave.comma.comma_backend.account.dto.AdminCreateForm;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.service.admin.AccountAdminService;
import com.know_wave.comma.comma_backend.account.service.auth.SignService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/account")
public class AccountAdminController {

    private final AccountAdminService accountAdminService;
    private final SignService signService;

    public AccountAdminController(AccountAdminService accountAdminService, SignService signService) {
        this.accountAdminService = accountAdminService;
        this.signService = signService;
    }

    @PostMapping("/manager")
    public ResponseEntity<String> specifyManager(@RequestBody @Valid AccountIdRequest accountDto) {
        accountAdminService.changeAccountRole(Role.MANAGER, accountDto.getAccountId());

        return ResponseEntity.ok("Specified " + Role.MANAGER.name() + " role, account : " + accountDto.getAccountId());
    }

    @DeleteMapping("/manager")
    public ResponseEntity<String> deSpecifyManager(@RequestBody @Valid AccountIdRequest accountDto) {
        accountAdminService.changeAccountRole(Role.MEMBER, accountDto.getAccountId());

        return ResponseEntity.ok("De-Specified " + Role.MANAGER.name() + " role, account : " + accountDto.getAccountId());
    }

    @PostMapping("/authority")
    public ResponseEntity<String> addCommunityAuthority(@RequestBody @Valid AccountIdRequest accountDto) {
        accountAdminService.changeAccountRole(Role.MEMBER, accountDto.getAccountId());

        return ResponseEntity.ok("Updated authority account : " + accountDto.getAccountId());
    }

    @DeleteMapping("/authority")
    public ResponseEntity<String> removeCommunityAuthority(@RequestBody @Valid AccountIdRequest accountDto) {
        accountAdminService.changeAccountRole(Role.MEMBER_NoEquipmentApplyAndCUD, accountDto.getAccountId());

        return ResponseEntity.ok("Updated authority account : " + accountDto.getAccountId());
    }

    @DeleteMapping("/equipment_apply_authority")
    public ResponseEntity<String> removeEquipmentApplyAuthority(@RequestBody @Valid AccountIdRequest accountDto) {
        accountAdminService.changeAccountRole(Role.MEMBER_NoEquipmentApply, accountDto.getAccountId());

        return ResponseEntity.ok("Updated authority account : " + accountDto.getAccountId());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUpManager(@RequestBody @Valid AdminCreateForm form) {
        signService.adminJoin(form);
        return new ResponseEntity<>("Created admin account", HttpStatus.CREATED);
    }
}
