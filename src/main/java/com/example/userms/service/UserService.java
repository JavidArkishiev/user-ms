package com.example.userms.service;

import com.example.userms.dto.request.ChangePasswordRequest;
import com.example.userms.dto.request.DeleteAccountDto;
import com.example.userms.dto.request.UserRequestDto;
import com.example.userms.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    void changePassword(ChangePasswordRequest request);

    UserResponseDto getUserProfile();

    List<UserResponseDto> getAllUsers();

    void deleteAccount(DeleteAccountDto dto, HttpServletRequest request);

    void updateUser(UserRequestDto requestDto);

    UserResponseDto getUserProfileByEmail(String email);
}
