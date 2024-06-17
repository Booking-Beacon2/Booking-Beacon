package com.bteam.Booking_Beacon.domain.booking.repository;

import com.bteam.Booking_Beacon.domain.booking.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
