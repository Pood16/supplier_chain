package org.tricol.supplierchain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tricol.supplierchain.dto.request.AssignRoleRequest;
import org.tricol.supplierchain.dto.request.ModifyPermissionRequest;
import org.tricol.supplierchain.dto.response.UserResponseDto;
import org.tricol.supplierchain.dto.response.UserWithPermissionsResponseDto;
import org.tricol.supplierchain.service.inter.UserAdminService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Page<UserResponseDto>> getUsers(
           @PageableDefault(size = 10, page = 0, sort = "username", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(userAdminService.getUsers(pageable));
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<UserWithPermissionsResponseDto> assignRole(
            @PathVariable Long id,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        UserWithPermissionsResponseDto response = userAdminService.assignRole(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<UserWithPermissionsResponseDto> modifyPermission(
            @PathVariable Long id,
            @Valid @RequestBody ModifyPermissionRequest request
    ) {
        UserWithPermissionsResponseDto response = userAdminService.modifyPermission(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Set<String>> getUserPermissions(@PathVariable Long id) {
        Set<String> permissions = userAdminService.getUserEffectivePermissions(id);
        return ResponseEntity.ok(permissions);
    }
}
