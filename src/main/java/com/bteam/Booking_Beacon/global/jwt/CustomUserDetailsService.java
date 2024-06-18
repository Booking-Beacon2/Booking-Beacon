package com.bteam.Booking_Beacon.global.jwt;

import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final JwtUserRepository jwtUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        // userId 로 디비에서 유저 정보 조회
        UserEntity user = this.jwtUserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 조회한 유저 정보를 가지고 토큰 dto 생성
        JwtUserInfo jwtUserInfo = JwtUserInfo.builder().userId(user.getUserId()).username(user.getUserName()).build();

        return new CustomUserDetails(jwtUserInfo);
    }
}
