package com.bteam.Booking_Beacon.domain.auth.service;

import com.bteam.Booking_Beacon.domain.auth.dto.request.*;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreatePartnerRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.CreateUserRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.TokenRes;
import com.bteam.Booking_Beacon.domain.auth.dto.response.VerifyEmailRes;
import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.domain.auth.repository.PartnerRepository;
import com.bteam.Booking_Beacon.domain.auth.repository.UserRepository;
import com.bteam.Booking_Beacon.global.config.AuthConfig;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
import com.bteam.Booking_Beacon.global.exception.RestApiException;
import com.bteam.Booking_Beacon.global.exception.UnHandledUserException;
import com.bteam.Booking_Beacon.global.jwt.JwtUtil;
import com.bteam.Booking_Beacon.global.jwt.JwtUserInfo;
import com.bteam.Booking_Beacon.global.util.EmailService;
import com.bteam.Booking_Beacon.global.util.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /* 랜덤 인증 코드 생성 */
    public String generateRandomAuthCode() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        return randomNumber;
    }

    /**
     * @description  전체 유저 정보 조회
     */
    public ResponseEntity<List<UserEntity>> getUsers() {
        List<UserEntity> users = userRepository.findAll().reversed();
        return ResponseEntity.ok(users);
    }

    /**
     * @description  본인 유저 정보 조회
     */
    public ResponseEntity<Optional<UserEntity>> getUser(long userId) {
        Optional<UserEntity> user =  userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(user);
    }

    /**
     * @description 인증 메일 전송
     */
    public ResponseEntity<VerifyEmailRes> sendVerifyEmail(String userEmail) {
        UserEntity user = this.userRepository.findUserByEmail(userEmail);
        if (user != null) {
            throw new RestApiException(CommonErrorCode.BB_EMAIL_ALREADY_EXIST);
        }

        String authCode = generateRandomAuthCode();
        emailService.sendEmail(userEmail, "이메일 인증 코드입니다.", "3분 안에 인증번호 입력란에 아래 번호를 입력해 주세요.<br>" + authCode);
        redisService.setValue("verify_" + userEmail, authCode, 180, TimeUnit.SECONDS );
        return ResponseEntity.ok(VerifyEmailRes.builder().authCode(authCode).build());
    }

    public ResponseEntity<VerifyEmailRes> verifyEmailAuthCode(String userEmail) {
        UserEntity user = this.userRepository.findUserByEmail(userEmail);
        if (user != null) {
            throw new RestApiException(CommonErrorCode.BB_EMAIL_ALREADY_EXIST);
        }

        Object authCode = redisService.getValue("verify_" + userEmail);
        if (authCode == null) {
            throw new RestApiException(CommonErrorCode.BB_VERIFY_AUTH_CODE_NOT_FOUND);
        }
        return ResponseEntity.ok(VerifyEmailRes.builder().authCode(authCode.toString()).build());
    }

    /**
     *
     * @description 로그인
     */
    public ResponseEntity<TokenRes> login(LoginReq loginReq) {
        UserEntity user = this.userRepository.findUserByEmail(loginReq.getUserEmail());
        if (user == null || user.getPassword().isEmpty()) {
            throw new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND);
        }

        if (!authConfig.passwordEncoder().matches(loginReq.getPassword(), user.getPassword())) {
            throw new RestApiException(CommonErrorCode.BB_PASSWORD_INCORRECT);
        }

        JwtUserInfo jwtUserInfo = JwtUserInfo.builder().userId(user.getUserId()).username(user.getUserName()).build();

        String accessToken = this.jwtTokenUtil.createToken(jwtUserInfo, "access");
        String refreshToken = this.jwtTokenUtil.createToken(jwtUserInfo, "refresh");
        TokenRes tokenRes = TokenRes
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(tokenRes);
    }

    /**
     * @description refresh access token
     */
    public ResponseEntity<TokenRes> refreshAccessToken(String refreshToken) {
        JwtUserInfo userInfo = this.jwtTokenUtil.getUserFromToken(refreshToken);

        long now = (new Date()).getTime();
        long exp = userInfo.getExp();

        if (now > exp) {
            throw new RestApiException(CommonErrorCode.TOKEN_EXPIRATION);
        }

        JwtUserInfo jwtUserInfo = JwtUserInfo.builder().userId(userInfo.getUserId()).username(userInfo.getUsername()).build();
        String access = this.jwtTokenUtil.createToken(jwtUserInfo, "access");
        String refresh = this.jwtTokenUtil.createToken(jwtUserInfo, "refresh");

        TokenRes tokenRes = TokenRes.builder().accessToken(access).refreshToken(refresh).build();
        return ResponseEntity.ok(tokenRes);
    }

    /**
     *
     * @description 에러 처리용 테스트
     */
    public Object userError() {
        throw new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND);
    }

    public Object unhandledError() {
        throw new UnHandledUserException("이상합니다");
    }

    /**
     *
     * @description 회원가입
     */
    @Transactional
    public ResponseEntity<CreateUserRes> createUser(CreateUserReq createUserReq) {
        UserEntity user = this.userRepository.findUserByEmail(createUserReq.getUserEmail());
        if (user != null) {
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
    public void updateUser(UpdateUserReq updateUserReq) {
        UserEntity userEntity
                = userRepository.findById(updateUserReq.getUserId())
                .orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));
        userEntity.setUserName(updateUserReq.getUserName());
    }

    /** @description 파트너 등록 */
    @Transactional
    public ResponseEntity<CreatePartnerRes> createPartner(Long userId, CreatePartnerReq createPartnerReq) {
        PartnerEntity partner = this.partnerRepository.findPartnerByEin(createPartnerReq.getEin());

        if (partner != null) {
            throw new RestApiException(CommonErrorCode.BB_PARTNER_ALREADY_EXIST);
        }

        UserEntity user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));

        PartnerEntity partnerEntity = PartnerEntity.builder()
                .partnerName(createPartnerReq.getPartnerName())
                .user(user)
                .ein(createPartnerReq.getEin())
                .phoneNumber(createPartnerReq.getPhoneNumber())
                .build();

        Long partnerId = this.partnerRepository.save(partnerEntity).getPartnerId();
        CreatePartnerRes res = CreatePartnerRes.builder().partnerId(partnerId).build();
        return ResponseEntity.ok().body(res);
    }

    /** @description 파트너 수정 */
    @Transactional
    public void updatePartner(Long userId, UpdatePartnerReq updatePartnerReq) {
        userRepository.findById(userId).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));

        PartnerEntity partner = partnerRepository.findPartnerByUserId(userId).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));

        updatePartnerReq.getPartnerName().ifPresent(partner::setPartnerName);
        updatePartnerReq.getPhoneNumber().ifPresent(partner::setPhoneNumber);
        updatePartnerReq.getEin().ifPresent(partner::setEin);
    }

    @Transactional
    public void deletePartner(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_USER_NOT_FOUND));
        PartnerEntity partner = partnerRepository.findPartnerByUserId(userId).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));
        partnerRepository.delete(partner);
    }

    /** @description  전체 파트너 목록 조회 */
    public List<PartnerEntity> getPartners() {
        return partnerRepository.findAll().reversed();
    }

    /** @description  본인 파트너 정보 조회 */
    public PartnerEntity getPartner(long userId) {
        return partnerRepository.findPartnerByUserId(userId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));
    }
}
