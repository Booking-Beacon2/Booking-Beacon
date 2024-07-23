package com.bteam.Booking_Beacon.domain.auth.service;

import com.bteam.Booking_Beacon.domain.auth.dto.request.*;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreatePartnerRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreateUserRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.TokenRes;
import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.domain.auth.repository.PartnerRepository;
import com.bteam.Booking_Beacon.domain.auth.repository.UserRepository;
import com.bteam.Booking_Beacon.global.config.AuthConfig;
import com.bteam.Booking_Beacon.global.constant.UserType;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
import com.bteam.Booking_Beacon.global.exception.RestApiException;
import com.bteam.Booking_Beacon.global.jwt.JwtUtil;
import com.bteam.Booking_Beacon.global.jwt.JwtPayload;
import com.bteam.Booking_Beacon.global.util.EmailService;
import com.bteam.Booking_Beacon.global.util.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;
    private final JwtUtil jwtTokenUtil;
    private final AuthConfig authConfig;
    private final RedisService redisService;
    private final EmailService emailService;

    /** 랜덤 인증 코드 생성 */
    public String generateRandomAuthCode() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        return randomNumber;
    }

    /** 동일 이메일로 가입한 개인회원/파트너회원 존재하는지 검사 */
    private Boolean validateEmailIsExists(UserType userType, String email) {
        switch (userType) {
            case USER -> { return this.userRepository.findUserByEmail(email).isPresent(); }
            case PARTNER -> { return this.partnerRepository.findPartnerByEmail(email).isPresent(); }
            default -> { return true; }
        }
    }

    /** 인증 메일 전송 */
    public void sendVerifyEmail(VerifyEmailReq verifyEmailReq) {
        boolean isExists = this.validateEmailIsExists(verifyEmailReq.getUserType(), verifyEmailReq.getEmail());
        if (isExists) {
            throw new RestApiException(CommonErrorCode.BB_EMAIL_ALREADY_EXIST);
        }

        final String authCode = generateRandomAuthCode();
        final String prefix = verifyEmailReq.getUserType() + "_VERIFY_";
        emailService.sendEmail(verifyEmailReq.getEmail(), "이메일 인증 코드입니다.", "3분 안에 인증번호 입력란에 아래 번호를 입력해 주세요.<br>" + authCode);
        redisService.setValue(prefix + verifyEmailReq.getEmail(), authCode, 180, TimeUnit.SECONDS );
    }

    /** 인증 메일 인증코드 일치 검사 */
    public ResponseEntity<Boolean> verifyEmailAuthCode(VerifyEmailAuthCodeReq verifyEmailAuthCodeReq) {
        boolean isExists = this.validateEmailIsExists(verifyEmailAuthCodeReq.getUserType(), verifyEmailAuthCodeReq.getEmail());
        if (isExists) {
            throw new RestApiException(CommonErrorCode.BB_EMAIL_ALREADY_EXIST);
        }

        final String prefix = verifyEmailAuthCodeReq.getUserType() + "_VERIFY_";
        Object authCode = redisService.getValue(prefix + verifyEmailAuthCodeReq.getEmail());
        if (authCode == null) {
            throw new RestApiException(CommonErrorCode.BB_VERIFY_AUTH_CODE_NOT_FOUND);
        }

        if (!authCode.toString().equals(verifyEmailAuthCodeReq.getAuthCode())) {
            throw new RestApiException(CommonErrorCode.BB_VERIFY_AUTH_CODE_NOT_EQUAL);
        } else {
            return ResponseEntity.ok(true);
        }
    }

    /** 로그인 */
    public ResponseEntity<TokenRes> login(LoginReq loginReq) {
        String password = "";
        JwtPayload jwtPayload = null;
        switch (loginReq.getUserType()) {
            case USER -> {
                UserEntity user = this.userRepository.findUserByEmail(loginReq.getUserEmail()).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));
                password = user.getPassword();
                jwtPayload = JwtPayload.builder()
                        .userType(UserType.USER)
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .build();
            }
            case PARTNER -> {
                PartnerEntity partner = this.partnerRepository.findPartnerByEmail(loginReq.getUserEmail()).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));
                password = partner.getPassword();
                jwtPayload = JwtPayload.builder()
                        .userType(UserType.PARTNER)
                        .partnerId(partner.getPartnerId())
                        .email(partner.getEmail())
                        .build();
            }
        }

        if (!authConfig.passwordEncoder().matches(loginReq.getPassword(), password)) {
            throw new RestApiException(CommonErrorCode.BB_PASSWORD_INCORRECT);
        }

        String accessToken = this.jwtTokenUtil.createToken(jwtPayload, "access");
        String refreshToken = this.jwtTokenUtil.createToken(jwtPayload, "refresh");
        TokenRes tokenRes = TokenRes
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(tokenRes);
    }

    /** refresh access token */
    public ResponseEntity<TokenRes> refreshAccessToken(String refreshToken) {
        JwtPayload userInfo = this.jwtTokenUtil.getJwtPayloadFromToken(refreshToken);

        long now = System.currentTimeMillis();
        long exp = userInfo.getExp();
        Instant now_i = Instant.ofEpochMilli(now);
        Instant exp_i = Instant.ofEpochSecond(exp);

        if (now_i.isAfter(exp_i)) {
            throw new RestApiException(CommonErrorCode.TOKEN_EXPIRATION);
        }

        JwtPayload jwtPayload = JwtPayload.builder().userType(userInfo.getUserType()).userId(userInfo.getUserId()).partnerId(userInfo.getPartnerId()).email(userInfo.getEmail()).build();
        String access = this.jwtTokenUtil.createToken(jwtPayload, "access");
        String refresh = this.jwtTokenUtil.createToken(jwtPayload, "refresh");

        TokenRes tokenRes = TokenRes.builder().accessToken(access).refreshToken(refresh).build();
        return ResponseEntity.ok(tokenRes);
    }

    /* ************* USER ************* */
    /** 개인 회원 가입 */
    @Transactional
    public ResponseEntity<CreateUserRes> createUser(CreateUserReq createUserReq) {
        boolean isExistsUser = this.userRepository.findUserByEmail(createUserReq.getEmail()).isPresent();
        if (isExistsUser) {
            throw new RestApiException(CommonErrorCode.BB_EMAIL_ALREADY_EXIST);
        }

        String encryptedPassword = authConfig.passwordEncoder().encode(createUserReq.getPassword());
        createUserReq.setPassword(encryptedPassword);
        Long userId = this.userRepository.save(createUserReq.toEntity()).getUserId();
        CreateUserRes createUserRes = CreateUserRes.builder().userId(userId).build();
        return ResponseEntity.ok().body(createUserRes);
    }

    /**
     * @description 회원 정보 수정
     * dirty checking : 엔티티를 조회 하고 조회 한 엔티티를 변경하면 자동으로 변경된 사항이 db 에 반영된다.
     * JPA는 엔티티를 조회할 때 해당 엔티티의 상태를 기반으로 하나의 스냅샷을 만듭니다.
     * 그 후 트랜잭션이 종료되는 시점에서 만들어놓은 스냅샷과 비교하여 변경을 감지합니다.
     * 만약 변경이 감지되었다면, 수정(Update) 쿼리를 데이터베이스에 전달합니다.
     */
    @Transactional
    public void updateUser(Optional<Long> userId, UpdateUserReq updateUserReq) {
        UserEntity userEntity
                = userRepository.findById(userId.orElseThrow(() -> new RestApiException(CommonErrorCode.BB_HAS_NONE_USER_ID)))
                .orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));

        if (updateUserReq.getUserName() != null) {
            userEntity.setUserName(updateUserReq.getUserName());
        }

        if (updateUserReq.getPassword() != null) {
            String encryptedPassword = authConfig.passwordEncoder().encode(updateUserReq.getPassword());
            updateUserReq.setPassword(encryptedPassword);
        }
    }

    /** 전체 유저 목록 조회 */
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /** 본인 유저 정보 조회 */
    public ResponseEntity<Optional<UserEntity>> getUser(Optional<Long> userId) {
        Optional<UserEntity> user =  userRepository.findById(userId.orElseThrow(() -> new RestApiException(CommonErrorCode.BB_HAS_NONE_USER_ID)));
        if (user.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(user);
    }

//    /** 개인 회원 탈퇴 */
//    public void withdrawUser(long userId) {
//        UserEntity user = userRepository.fineUserByUserId(userId).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));
//        userRepository.delete(user);
//    }

    /* ************* PARTNER ************* */
    /** 파트너 회원가입 */
    @Transactional
    public ResponseEntity<CreatePartnerRes> createPartner(CreatePartnerReq createPartnerReq) {
        boolean isExistsEmail = partnerRepository.findPartnerByEmail(createPartnerReq.getEmail()).isPresent();
        boolean isExistsEin = partnerRepository.findPartnerByEin(createPartnerReq.getEin()).isPresent();
        if (isExistsEmail) {
            throw new RestApiException(CommonErrorCode.BB_EMAIL_ALREADY_EXIST);
        }
        if (isExistsEin) {
            throw new RestApiException(CommonErrorCode.BB_EIN_ALREADY_EXIST);
        }

        String encryptedPassword = authConfig.passwordEncoder().encode(createPartnerReq.getPassword());
        createPartnerReq.setPassword(encryptedPassword);

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerName(createPartnerReq.getPartnerName())
                .userName(createPartnerReq.getUserName())
                .email(createPartnerReq.getEmail())
                .password(createPartnerReq.getPassword())
                .ein(createPartnerReq.getEin())
                .phoneNumber(createPartnerReq.getPhoneNumber())
                .build();

        Long partnerId = this.partnerRepository.save(partnerEntity).getPartnerId();
        CreatePartnerRes res = CreatePartnerRes.builder().partnerId(partnerId).build();
        return ResponseEntity.ok().body(res);
    }

    /** 파트너 수정 */
    @Transactional
    public void updatePartner(Optional<Long> partnerId, UpdatePartnerReq updatePartnerReq) {
        PartnerEntity partner = partnerRepository.findPartnerByPartnerId(partnerId.orElseThrow(() -> new RestApiException(CommonErrorCode.BB_HAS_NONE_PARTNER_ID))).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));

        if (updatePartnerReq.getPartnerName() != null) {
            partner.setPartnerName(updatePartnerReq.getPartnerName());
        }

        if (updatePartnerReq.getEin() != null) {
            boolean isExistsEin = partnerRepository.findPartnerByEin(updatePartnerReq.getEin()).isPresent();
            if (isExistsEin) {
                throw new RestApiException(CommonErrorCode.BB_EIN_ALREADY_EXIST);
            }
            partner.setEin(updatePartnerReq.getEin());
        }

        if (updatePartnerReq.getPhoneNumber() != null) {
            partner.setPhoneNumber(updatePartnerReq.getPhoneNumber());
        }
    }

//    /** 파트너 탈퇴 */
//    @Transactional
//    public void withdrawPartner(Long partnerId) {
//        PartnerEntity partner = partnerRepository.findPartnerByPartnerId(partnerId).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));
//        partnerRepository.delete(partner);
//    }

    /** 전체 파트너 목록 조회 */
    public ResponseEntity<List<PartnerEntity>> getPartners() {
        List<PartnerEntity> partners = partnerRepository.findAll();
        return ResponseEntity.ok(partners);
    }

    /** 본인 파트너 정보 조회 */
    public ResponseEntity<Optional<PartnerEntity>> getPartner(Optional<Long> partnerId) {
        Optional<PartnerEntity> partner =  partnerRepository.findPartnerByPartnerId(partnerId.orElseThrow(() -> new RestApiException(CommonErrorCode.BB_HAS_NONE_PARTNER_ID)));
        if (partner.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(partner);
    }
}
