package com.bteam.Booking_Beacon.domain.booking.repository;

import com.bteam.Booking_Beacon.domain.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

}