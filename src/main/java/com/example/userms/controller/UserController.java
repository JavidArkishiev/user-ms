package com.example.userms.controller;

import com.example.userms.dto.request.ChangePasswordRequest;
import com.example.userms.dto.request.DeleteAccountDto;
import com.example.userms.dto.request.UserRequestDto;
import com.example.userms.dto.response.BaseResponse;
import com.example.userms.dto.response.UserResponseDto;
import com.example.userms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    private final UserService userService;

    @PatchMapping("change-password")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return BaseResponse.success("Password has been changed successfully");
    }

    @GetMapping("user-profile")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UserResponseDto> getUserProfile() {
        return BaseResponse.oK(userService.getUserProfile());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<UserResponseDto>> getAllUsers() {
        return BaseResponse.oK(userService.getAllUsers());

    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> deleteAccount(@Valid @RequestBody DeleteAccountDto dto, HttpServletRequest request) {
        userService.deleteAccount(dto, request);
        return BaseResponse.success("Your account deleted. " +
                "See you soon ))");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> updateUser(@Valid @RequestBody UserRequestDto requestDto) {
        userService.updateUser(requestDto);
        return BaseResponse.success("User updated successfully");
    }

}
