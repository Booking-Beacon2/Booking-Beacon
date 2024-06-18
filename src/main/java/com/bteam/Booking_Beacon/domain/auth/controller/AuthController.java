package com.bteam.Booking_Beacon.domain.auth.controller;

import com.bteam.Booking_Beacon.domain.auth.dto.request.*;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreatePartnerRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreateUserRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.TokenRes;
import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /***** USER *****/
    @GetMapping("users")
    @Operation(summary = "유저 리스트 조회")
    public ResponseEntity<List<UserEntity>> getUsers(@RequestAttribute("userId") Long userId) {
        return authService.getUsers();
    }

    @GetMapping("user")
    @Operation(summary = "본인 정보 조회")
    public ResponseEntity<Optional<UserEntity>> getUser(@RequestAttribute("userId") Long userId) {
        return authService.getUser(userId);
    }

    @PostMapping("login")
    @Operation(summary = "로그인", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRes.class))
            })
    })
    public ResponseEntity<TokenRes> login(@Validated @RequestBody LoginReq loginReq, TokenRes tokenRes) {
        return this.authService.login(loginReq);
    }

    @GetMapping("user-error")
    public Object error() {
        return this.authService.userError();
    }

    @GetMapping("unhandled-error")
    public Object unhandledError() {
        return this.authService.unhandledError();
    }

    @PostMapping("join")
    @Operation(summary = "회원가입")
    public ResponseEntity<CreateUserRes> createUser(@RequestBody CreateUserReq createUserReq) {
        return this.authService.createUser(createUserReq);
    }

    @PutMapping("user")
    @Operation(summary = "본인 정보 수정")
    public void updateUser(@RequestBody UpdateUserReq updateUserReq) {
        this.authService.updateUser(updateUserReq);
    }

    /***** PARTNER *****/
    @PostMapping("partner")
    public ResponseEntity<CreatePartnerRes> createPartner(@RequestAttribute("userId") Long userId, @RequestBody CreatePartnerReq createPartnerReq) {
        return this.authService.createPartner(userId, createPartnerReq);
    }

    @PutMapping("partner")
    public void updatePartner(@RequestAttribute("userId") Long userId, @RequestBody UpdatePartnerReq updatePartnerReq) {
        this.authService.updatePartner(userId, updatePartnerReq);
    }

    @DeleteMapping("partner")
    public void deletePartner(@RequestAttribute("userId") Long userId) {
        this.authService.deletePartner(userId);
    }
}
