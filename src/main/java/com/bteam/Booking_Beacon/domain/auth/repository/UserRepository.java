package com.bteam.Booking_Beacon.domain.auth.repository;


import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select * from user where user_email = ?1", nativeQuery = true)
    Optional<UserEntity> findUserByEmail(String email);
}
