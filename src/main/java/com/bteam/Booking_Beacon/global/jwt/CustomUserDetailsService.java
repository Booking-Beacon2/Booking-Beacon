package com.bteam.Booking_Beacon.global.jwt;

import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import com.bteam.Booking_Beacon.domain.auth.repository.PartnerRepository;
import com.bteam.Booking_Beacon.global.constant.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final JwtUserRepository jwtUserRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadJwtPayloadById(UserType userType, Long accountId) throws UsernameNotFoundException {
        JwtPayload jwtPayload = null;
        switch (userType) {
            case USER -> {
                UserEntity user = jwtUserRepository.findById(accountId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                jwtPayload = JwtPayload.builder().userType(UserType.USER).userId(accountId).email(user.getEmail()).build();
            }
            case PARTNER -> {
                PartnerEntity partner = partnerRepository.findPartnerByPartnerId(accountId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                jwtPayload = JwtPayload.builder().userType(UserType.PARTNER).userId(accountId).email(partner.getEmail()).build();
            }
        }

        return new CustomUserDetails(jwtPayload);
    }
}
