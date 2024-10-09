package com.example.userms.service;

import com.example.userms.dto.request.ChangePasswordRequest;
import com.example.userms.dto.request.DeleteAccountDto;
import com.example.userms.dto.request.UserRequestDto;
import com.example.userms.dto.response.UserResponseDto;
import com.example.userms.entity.User;
import com.example.userms.exception.DataExistException;
import com.example.userms.exception.DataNotFoundException;
import com.example.userms.model.UserMapper;
import com.example.userms.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    @Override
    public void changePassword(ChangePasswordRequest request) {
        var principal = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) principal.getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new DataExistException("Current password is wrong");
        }
        if (!request.getNewPassword().matches(request.getConfirmNewPassword())) {
            throw new DataExistException("Both of password must same");
        }
        user.setPassword(passwordEncoder.encode(request.getConfirmNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponseDto getUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return userMapper.mapToDto(user);

    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.mapToDtoList(userList);
    }

    @Override
    public void deleteAccount(DeleteAccountDto dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        var email = jwtService.extractUsername(token);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new DataExistException("Current password incorrect");
        }
        userRepository.delete(user);

    }

    @Override
    public void updateUser(UserRequestDto requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var oldUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        var updatedUser = userMapper.mapToEntity(oldUser, requestDto);
        userRepository.save(updatedUser);

    }

}
