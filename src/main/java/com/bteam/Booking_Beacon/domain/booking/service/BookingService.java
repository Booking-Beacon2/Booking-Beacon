package com.bteam.Booking_Beacon.domain.booking.service;


import com.bteam.Booking_Beacon.domain.booking.dto.RegisterEventReq;
import com.bteam.Booking_Beacon.domain.booking.dto.RegisterEventRes;
import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import com.bteam.Booking_Beacon.domain.booking.repository.ConcertRepository;
import com.bteam.Booking_Beacon.domain.booking.repository.MusicalRepository;
import com.bteam.Booking_Beacon.global.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class BookingService {
    private final MusicalRepository musicalRepository;
    private final ConcertRepository concertRepository;

    @Transactional
    public ResponseEntity<RegisterEventRes> registerBookingEvents(RegisterEventReq registerEventReq) {
        log.info(String.valueOf(registerEventReq.getEventType()));
        RegisterEventRes res = null;
        if(registerEventReq.getEventType() == EventType.MUSICAL){
            Long musicalId = this.musicalRepository.save(registerEventReq.toMusicalEntity()).getMusicalId();
            res = RegisterEventRes.builder().musicalId(musicalId).build();
        } else if (registerEventReq.getEventType() == EventType.CONCERT) {
            Long concertId = this.concertRepository.save(registerEventReq.toConcertEntity()).getConcertId();
            res = RegisterEventRes.builder().concertId(concertId).build();
        }

        return ResponseEntity.ok().body(res);

    }

    public ResponseEntity<List<MusicalEntity>> getMusicalBookings() {
        List<MusicalEntity> musicals = this.musicalRepository.findAll().reversed();
        return ResponseEntity.ok().body(musicals);
    }
}
