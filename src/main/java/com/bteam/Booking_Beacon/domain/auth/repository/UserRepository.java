package com.bteam.Booking_Beacon.domain.auth.repository;


import com.bteam.Booking_Beacon.domain.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select * from user where user_email = ?1", nativeQuery = true)
    UserEntity findUserByEmail(String email);
}
