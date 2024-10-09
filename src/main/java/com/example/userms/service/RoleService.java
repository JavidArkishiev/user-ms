package com.example.userms.service;

import com.example.userms.dto.request.RoleRequestDto;
import com.example.userms.entity.Role;
import com.example.userms.entity.User;
import com.example.userms.exception.DataExistException;
import com.example.userms.exception.DataNotFoundException;
import com.example.userms.model.RoleMapper;
import com.example.userms.repository.RoleRepository;
import com.example.userms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;


    public void createRole(RoleRequestDto role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new DataExistException("This role already exist");
        }
        Role roleEntity = roleMapper.mapToRoleEntity(role);
        roleRepository.save(roleEntity);
    }

    public Role findById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();

    }

    public void assignUserToRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        if (user.getRoles().contains(role)) {
            throw new DataExistException(user.getEmail() + " already " + role.getName() + " assigned");
        }
        role.getUsers().add(user);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public List<Role> getAllRolesUser(String email) {
        return userRepository.findAllByEmail(email);
    }


    public void removeUserFromRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (!user.getRoles().contains(role)) {
            throw new DataExistException("This role already assigned to user");
        }
        user.getRoles().remove(role);
        role.getUsers().remove(user);
        roleRepository.save(role);

    }

    public void removeAllUserFromRole(Long roleId) {
        var role = roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if (role.getUsers().isEmpty()) {
            throw new DataExistException("This role has deleted all from user");

        }
        role.getUsers().forEach(user -> user.getRoles().remove(role));

        roleRepository.save(role);
    }


    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        roleRepository.delete(role);

    }
}
