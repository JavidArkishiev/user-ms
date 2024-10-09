package com.example.userms.controller;

import com.example.userms.dto.request.*;
import com.example.userms.dto.response.AccessTokenResponse;
import com.example.userms.dto.response.AuthResponse;
import com.example.userms.dto.response.BaseResponse;
import com.example.userms.dto.response.UuidResponse;
import com.example.userms.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest) throws MessagingException {
        authService.signUp(signUpRequest);
        return BaseResponse.success("Otp code sent to e-mail address");
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws MessagingException {
        return BaseResponse.oK(authService.login(loginRequest));
    }

    @PostMapping("resend-otp")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> resetOtp(@RequestParam String email) throws MessagingException {
        authService.regenerateOtp(email);
        return BaseResponse.success("Otp code sent to e-mail address");
    }

    @PostMapping("verify-account")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> verifyAccount(@RequestBody @Valid OtpDto otpDto) {
        authService.verifyAccount(otpDto);
        return BaseResponse.success("Your account already verified. " +
                "You can enter app");
    }

    @PostMapping("forget-password")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> forgetPassword(@RequestParam String email) throws MessagingException {
        authService.forgetPassword(email);
        return BaseResponse.success("Otp code sent to e-mail address");
    }

    @PostMapping("verify-otp")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UuidResponse> verifyOtp(@RequestBody @Valid OtpDto otpDto) {
        return BaseResponse.oK(authService.verifyOtp(otpDto));
    }

    @PostMapping("reset-password/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> resetPassword(@PathVariable String uuid,
                                              @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(uuid, resetPasswordRequest);
        return BaseResponse.success("Password has been update. " +
                "You can enter app with new password");
    }

    @PostMapping("refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<AccessTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return BaseResponse.oK(authService.refreshToken(request));
    }
}
