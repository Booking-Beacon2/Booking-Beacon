package com.bteam.Booking_Beacon.global.jwt;

import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtPartnerRepository extends JpaRepository<PartnerEntity, Long> {
}
