package com.bteam.Booking_Beacon.domain.booking.service;


import com.bteam.Booking_Beacon.domain.booking.dto.RegisterMusicalReq;
import com.bteam.Booking_Beacon.domain.booking.dto.RegisterMusicalRes;
import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import com.bteam.Booking_Beacon.domain.booking.repository.MusicalRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@AllArgsConstructor
@Service
public class BookingService {
    private final MusicalRepository musicalRepository;

    @Transactional
    public ResponseEntity<RegisterMusicalRes> registerMusicalBooking(RegisterMusicalReq registerMusicalReq) {
        Long musicalId = this.musicalRepository.save(registerMusicalReq.toEntity()).getMusicalId();
        RegisterMusicalRes res = RegisterMusicalRes.builder().musicalId(musicalId).build();
        return ResponseEntity.ok().body(res);
    }

    public ResponseEntity<List<MusicalEntity>> getMusicalBookings() {
        List<MusicalEntity> musicals = this.musicalRepository.findAll().reversed();
        return ResponseEntity.ok().body(musicals);
    }
}
