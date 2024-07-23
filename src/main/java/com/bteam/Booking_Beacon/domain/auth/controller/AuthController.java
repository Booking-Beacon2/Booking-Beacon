package com.bteam.Booking_Beacon.domain.auth.controller;

import com.bteam.Booking_Beacon.domain.auth.dto.request.*;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreatePartnerRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreateUserRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.TokenRes;
import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.domain.auth.service.AuthService;
import com.bteam.Booking_Beacon.global.constant.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("auth")
@Tag(name = "Auth API")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("info")
    @Operation(summary = "info")
    public void info() {
        log.info("auth-service has initialized!");
    }

    /***** COMMON *****/
    @Operation(summary = "이메일 인증 코드 메일 발송")
    @PostMapping("verify/email")
    public void sendVerifyEmail(@Valid @RequestBody VerifyEmailReq verifyEmailReq) {
        this.authService.sendVerifyEmail(verifyEmailReq);
    }

    @Operation(summary = "이메일 인증 코드 검증")
    @GetMapping("verify/email")
    public ResponseEntity<Boolean> verifyEmailAuthCode(@Validated @RequestParam String authCode, @RequestParam String email, @RequestParam UserType userType) {
        return this.authService.verifyEmailAuthCode(VerifyEmailAuthCodeReq.builder().authCode(authCode).email(email).userType(userType).build());
    }

    /***** USER *****/
    @PostMapping("join")
    @Operation(summary = "개인 회원가입")
    public ResponseEntity<CreateUserRes> createUser(@RequestBody CreateUserReq createUserReq) {
        return this.authService.createUser(createUserReq);
    }

    @PostMapping("login")
    @Operation(summary = "로그인", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRes.class))
            })
    })
    public ResponseEntity<TokenRes> login(@Validated @RequestBody LoginReq loginReq) {
        return this.authService.login(loginReq);
    }

    @PostMapping("refresh-token")
    @Operation(summary = "token refresh with refreshToken", responses = {@ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRes.class))
        })
    })
    public ResponseEntity<TokenRes> refreshAccessToken(@Validated  @RequestBody RefreshAccessTokenReq refreshAccessTokenReq) {
        return this.authService.refreshAccessToken(refreshAccessTokenReq.getRefreshToken());
    }

    @PutMapping("user")
    @Operation(summary = "본인 정보 수정")
    public void updateUser(@RequestAttribute("userId") Optional<Long> userId, @RequestBody UpdateUserReq updateUserReq) {
        this.authService.updateUser(userId, UpdateUserReq.builder().userName(updateUserReq.getUserName()).password(updateUserReq.getPassword()).build());
    }

    @GetMapping("user")
    @Operation(summary = "본인 정보 조회")
    public ResponseEntity<Optional<UserEntity>> getUser(@RequestAttribute("userId") Optional<Long> userId) {
        return authService.getUser(userId);
    }

    @GetMapping("users")
    @Operation(summary = "유저 리스트 조회")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return authService.getUsers();
    }

//    @DeleteMapping("user")
//    @Operation(summary = "개인 회원 탈퇴")
//    public void withdrawUser(@RequestAttribute("userId") Long userId) {
//        this.authService.withdrawUser(userId);
//    }

    /***** PARTNER *****/
    @PostMapping("join-partner")
    @Operation(summary = "파트너 회원가입")
    public ResponseEntity<CreatePartnerRes> createPartner(@RequestBody CreatePartnerReq createPartnerReq) {
        return this.authService.createPartner(createPartnerReq);
    }

    @PutMapping("partner")
    @Operation(summary = "파트너 정보 수정")
    public void updatePartner(@RequestAttribute("partnerId") Optional<Long> partnerId, @RequestBody UpdatePartnerReq updatePartnerReq) {
        this.authService.updatePartner(partnerId, updatePartnerReq);
    }

//    @Operation(summary = "파트너 탈퇴")
//    @DeleteMapping("partner")
//    public void withdrawPartner(@RequestAttribute("partnerId") Long partnerId) {
//        this.authService.withdrawPartner(partnerId);
//    }

    @GetMapping("partner")
    @Operation(summary = "파트너 정보 조회")
    public ResponseEntity<Optional<PartnerEntity>> getPartner(@RequestAttribute("partnerId") Optional<Long> partnerId) {
        return authService.getPartner(partnerId);
    }

    @GetMapping("partners")
    @Operation(summary = "파트너 리스트 조회")
    public ResponseEntity<List<PartnerEntity>> getPartners() {
        return authService.getPartners();
    }
}
