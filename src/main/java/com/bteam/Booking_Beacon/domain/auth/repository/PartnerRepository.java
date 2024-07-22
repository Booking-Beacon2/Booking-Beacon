package com.bteam.Booking_Beacon.domain.auth.repository;

import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    @Query(value = "select * from partner where ein = ?1", nativeQuery = true)
    Optional<PartnerEntity> findPartnerByEin(String ein);

    @Query(value =  "select * from partner where user_id = ?1", nativeQuery = true)
    Optional<PartnerEntity> findPartnerByUserId(Long userId);
}
