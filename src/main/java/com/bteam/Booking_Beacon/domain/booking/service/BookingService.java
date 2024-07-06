package com.bteam.Booking_Beacon.domain.booking.service;


import com.bteam.Booking_Beacon.domain.booking.dto.GetMusicalsRes;
import com.bteam.Booking_Beacon.domain.booking.dto.RegisterEventReq;
import com.bteam.Booking_Beacon.domain.booking.dto.RegisterEventRes;
import com.bteam.Booking_Beacon.domain.booking.entity.EventFileEntity;
import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import com.bteam.Booking_Beacon.domain.booking.repository.ConcertRepository;
import com.bteam.Booking_Beacon.domain.booking.repository.EventFileRepository;
import com.bteam.Booking_Beacon.domain.booking.repository.MusicalRepository;
import com.bteam.Booking_Beacon.global.constant.EventType;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
import com.bteam.Booking_Beacon.global.exception.RestApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
@Slf4j
public class BookingService {
    private final MusicalRepository musicalRepository;
    private final ConcertRepository concertRepository;
    private final EventFileRepository eventFileRepository;

    @Transactional
    public ResponseEntity<RegisterEventRes> registerBookingEvents(RegisterEventReq registerEventReq) {
        RegisterEventRes res = null;
        if (Objects.equals(registerEventReq.getEventType().toString(), EventType.MUSICAL.getValue())) {
            Long musicalId = this.musicalRepository.save(registerEventReq.toMusicalEntity()).getMusicalId();
            Long[] fileIds = registerEventReq.getFileIds();
            for (Long fileId : fileIds) {
                // 사전에 동일한 파일로 등록한 적이 있다.
                if (this.eventFileRepository.findMusicalFile(fileId) != null) {
                    throw new RestApiException(CommonErrorCode.BB_EVENT_FILE_ALREADY_UPLOADED);
                }
                this.eventFileRepository.saveMusicalFile(fileId, musicalId);
            }
            res = RegisterEventRes.builder().musicalId(musicalId).build();
        } else if (Objects.equals(registerEventReq.getEventType().toString(), EventType.CONCERT.getValue())) {
            Long concertId = this.concertRepository.save(registerEventReq.toConcertEntity()).getConcertId();

            Long[] fileIds = registerEventReq.getFileIds();
            for (Long fileId : fileIds) {
                if (this.eventFileRepository.findConcertFile(fileId) != null) {
                    throw new RestApiException(CommonErrorCode.BB_EVENT_FILE_ALREADY_UPLOADED);
                }
                this.eventFileRepository.saveConcertFile(fileId, concertId);
            }

            res = RegisterEventRes.builder().concertId(concertId).build();
        }

        return ResponseEntity.ok().body(res);

    }

    public ResponseEntity<List<GetMusicalsRes>> getMusicalBookings() {
        List<GetMusicalsRes> listRes = new ArrayList<>();
        List<MusicalEntity> musicals = this.musicalRepository.findAll().reversed();
        for (MusicalEntity musical : musicals) {
            Long musicalId = musical.getMusicalId();
            List<Long> fileIds = this.eventFileRepository.findFileIdsByMusicalId(musicalId);
            GetMusicalsRes res = GetMusicalsRes.builder().musical(musical).fileIds(fileIds).build();
            listRes.add(res);
        }
        return ResponseEntity.ok().body(listRes);
    }
}
