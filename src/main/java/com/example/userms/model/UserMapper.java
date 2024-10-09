package com.example.userms.model;

import com.example.userms.dto.request.SignUpRequest;
import com.example.userms.dto.request.UserRequestDto;
import com.example.userms.dto.response.UserResponseDto;
import com.example.userms.entity.Role;
import com.example.userms.entity.User;
import com.example.userms.exception.DataNotFoundException;
import com.example.userms.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    protected PasswordEncoder passwordEncoder;

    protected RoleRepository roleRepository;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Mapping(target = "createdBy", expression = "java(signUpRequest.getEmail())")
    @Mapping(target = "otp", expression = "java(generateRandomOtp())")
    @Mapping(target = "enabled", constant = "false")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(signUpRequest.getPassword()))")
    @Mapping(target = "roles", expression = "java(mapRole())")
    public abstract User mapToEntity(SignUpRequest signUpRequest);

    public List<Role> mapRole() {
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new DataNotFoundException("USER role not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    public String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public abstract List<UserResponseDto> mapToDtoList(List<User> userList);

    public abstract UserResponseDto mapToDto(User userEntity);

    public abstract User mapToEntity(@MappingTarget User oldUser, UserRequestDto userRequestDto);
}
