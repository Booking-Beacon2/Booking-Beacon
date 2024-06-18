package com.bteam.Booking_Beacon.global.jwt;

import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtUserRepository extends JpaRepository<UserEntity, Long> {
}
