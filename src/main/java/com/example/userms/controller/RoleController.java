package com.example.userms.controller;

import com.example.userms.dto.request.RoleRequestDto;
import com.example.userms.dto.response.BaseResponse;
import com.example.userms.entity.Role;
import com.example.userms.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("role")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {
    private final RoleService roleService;


    @PostMapping()
    @ResponseStatus(CREATED)
    public BaseResponse<String> crateRole(@RequestBody RoleRequestDto role) {
        roleService.createRole(role);
        return BaseResponse.success("New role has created");
    }

    @PostMapping("assign-role-to-user")
    @ResponseStatus(OK)
    public BaseResponse<String> assignUserToRole(@RequestParam Long userId,
                                                 @RequestParam Long roleId) {
        roleService.assignUserToRole(userId, roleId);
        return BaseResponse.success("Assign new role to user");
    }

    @GetMapping()
    @ResponseStatus(OK)
    public BaseResponse<List<Role>> getAllRoles() {
        return BaseResponse.oK(roleService.getAllRoles());
    }

    @GetMapping("/{roleId}")
    @ResponseStatus(OK)
    public BaseResponse<Role> getRoleById(@PathVariable Long roleId) {
        return BaseResponse.oK(roleService.findById(roleId));
    }

    @GetMapping("all-roles-user")
    @ResponseStatus(OK)
    public BaseResponse<List<Role>> getAllRolesUser(@RequestParam String email) {
        return BaseResponse.oK(roleService.getAllRolesUser(email));

    }

    @DeleteMapping("/{roleId}")
    @ResponseStatus(OK)
    public BaseResponse<String> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return BaseResponse.success("Role has deleted");
    }

    @DeleteMapping("remove-all-role-from-users/{roleId}")
    @ResponseStatus(OK)
    public BaseResponse<String> removeAllUsersFromRole(@PathVariable Long roleId) {
        roleService.removeAllUserFromRole(roleId);
        return BaseResponse.success("This role has deleted all from user");
    }

    @DeleteMapping("remove-role-from-user")
    @ResponseStatus(OK)
    public BaseResponse<String> removeUserFromRole(@RequestParam Long userId,
                                                   @RequestParam Long roleId) {
        roleService.removeUserFromRole(userId, roleId);
        return BaseResponse.success("Role has deleted from user");
    }
}
