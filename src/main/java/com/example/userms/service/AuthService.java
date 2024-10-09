package com.example.userms.service;

import com.example.userms.dto.request.*;
import com.example.userms.dto.response.AccessTokenResponse;
import com.example.userms.dto.response.AuthResponse;
import com.example.userms.dto.response.UuidResponse;
import com.example.userms.entity.User;
import com.example.userms.exception.DataExistException;
import com.example.userms.exception.DataNotFoundException;
import com.example.userms.model.UserMapper;
import com.example.userms.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DataExistException("This e-mail already exist");
        }
        if (!signUpRequest.getConfirmPassword().equals(signUpRequest.getPassword())) {
            throw new DataExistException("Both of password must same");
        }

        User userEntity = userMapper.mapToEntity(signUpRequest);
        userRepository.save(userEntity);
        emailService.sendVerificationEmail(signUpRequest.getEmail(), userEntity.getOtp());

    }

    public AuthResponse login(LoginRequest loginRequest) throws MessagingException {
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException("E-mail or password incorrect"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DataNotFoundException("E-mail or password incorrect");
        }
        if (!user.isEnabled()) {
            regenerateOtp(user.getEmail());
            throw new DataExistException("Your account is not active. " +
                    "Please activate your account. " +
                    "Otp code sent to e-mail address");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail()
                , loginRequest.getPassword()));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthResponse.builder().accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public void regenerateOtp(String email) throws MessagingException {
        String otp = userMapper.generateRandomOtp();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), otp);

    }

    public void verifyAccount(OtpDto otpDto) {
        var user = userRepository.findByOtp(otpDto.getOtp())
                .orElseThrow(() -> new DataNotFoundException("Otp not found or incorrect"));
        if (user.isEnabled()) {
            throw new DataExistException("Your account already verified");
        }
        if (Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() > 3 * 60) {
            throw new DataNotFoundException("Otp code expired. " +
                    "Regenerate otp code. Please");
        }
        user.setEnabled(true);
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);
    }

    public void forgetPassword(String email) throws MessagingException {
        regenerateOtp(email);
    }

    public UuidResponse verifyOtp(OtpDto otpDto) {
        var user = userRepository.findByOtp(otpDto.getOtp())
                .orElseThrow(() -> new DataNotFoundException("Otp not found or incorrect"));

        String uuid = String.valueOf(UUID.randomUUID());
        user.setUuid(uuid);
        user.setUuidGeneratedTimme(LocalDateTime.now());
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);
        return UuidResponse.builder()
                .uuid(uuid)
                .build();
    }

    public void resetPassword(String uuid, ResetPasswordRequest resetPasswordRequest) {
        var user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new DataNotFoundException("User not found or Uuid expired"));

        if (Duration.between(user.getUuidGeneratedTimme()
                , LocalDateTime.now()).getSeconds() > 3 * 60) {
            throw new DataExistException("Uuid expired");
        }
        if (!resetPasswordRequest.getNewPassword().matches(resetPasswordRequest.getConfirmNewPassword())) {
            throw new DataExistException("Both of password must same");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getConfirmNewPassword()));
        user.setUuid(null);
        user.setUuidGeneratedTimme(null);
        userRepository.save(user);
    }

    public AccessTokenResponse refreshToken(RefreshTokenRequest request) {
        var email = jwtService.extractUsername(request.getRefreshToken());

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        if (jwtService.isTokenValid(request.getRefreshToken(), user)) {
            var accessToken = jwtService.generateToken(user);
            return AccessTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        }
        return null;
    }
}
