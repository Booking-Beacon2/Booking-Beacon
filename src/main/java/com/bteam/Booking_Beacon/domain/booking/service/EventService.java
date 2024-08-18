package com.bteam.Booking_Beacon.domain.booking.service;

import com.bteam.Booking_Beacon.domain.auth.dto.request.CreateEventRes;
import com.bteam.Booking_Beacon.domain.auth.dto.request.CreateEventScheduleReq;
import com.bteam.Booking_Beacon.domain.auth.entity.PartnerEntity;
import com.bteam.Booking_Beacon.domain.auth.repository.PartnerRepository;
import com.bteam.Booking_Beacon.domain.booking.dto.CreateEventReq;
import com.bteam.Booking_Beacon.domain.booking.dto.GetEventsRes;
import com.bteam.Booking_Beacon.domain.booking.entity.EventEntity;
import com.bteam.Booking_Beacon.domain.booking.entity.EventScheduleEntity;
import com.bteam.Booking_Beacon.domain.booking.repository.EventRepository;
import com.bteam.Booking_Beacon.domain.booking.repository.EventScheduleRepository;
import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
import com.bteam.Booking_Beacon.global.exception.RestApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final EventScheduleRepository eventScheduleRepository;
    private final PartnerRepository partnerRepository;

    @Transactional
    public ResponseEntity<CreateEventRes> createEvent(CreateEventReq createEventReq, Optional<Long> partnerId) {
        PartnerEntity partner = partnerRepository.findPartnerByPartnerId(partnerId.orElseThrow(() -> new RestApiException(CommonErrorCode.BB_HAS_NONE_PARTNER_ID))).orElseThrow(() -> new RestApiException(CommonErrorCode.BB_PARTNER_NOT_FOUND));

        EventEntity eventEntity = EventEntity.builder()
                .eventType(createEventReq.getEventType())
                .title(createEventReq.getTitle())
                .description(createEventReq.getDescription())
                .address(createEventReq.getAddress())
                .detailAddress(createEventReq.getDetailAddress())
                .zipCode(createEventReq.getZipCode())
                .partner(partner)
                .build();

        EventEntity event = eventRepository.save(eventEntity);

        List<CreateEventScheduleReq> eventSchedules = createEventReq.getSchedules();
        List<EventScheduleEntity> eventScheduleEntityList = new ArrayList<>();
        for (CreateEventScheduleReq e : eventSchedules) {
            EventScheduleEntity eventSchedule = EventScheduleEntity.builder()
                    .event(event)
                    .startDate(e.getStartDate())
                    .runningTime(e.getRunningTime())
                    .capacity(e.getCapacity())
                    .build();
            eventScheduleEntityList.add(eventSchedule);
        }

        eventScheduleRepository.saveAll(eventScheduleEntityList);
        CreateEventRes res = CreateEventRes.builder().eventId(event.getId()).build();
        return ResponseEntity.ok().body(res);
    }

    public ResponseEntity<List<GetEventsRes>> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventEntity> pageResult = eventRepository.findAll(pageable);
        List<GetEventsRes> events = pageResult.getContent().stream().map(GetEventsRes::of).toList();
        return ResponseEntity.ok().body(events);
    }
}
