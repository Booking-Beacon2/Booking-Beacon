package com.bteam.Booking_Beacon.domain.booking.controller;

import com.bteam.Booking_Beacon.domain.auth.dto.request.CreateEventRes;
import com.bteam.Booking_Beacon.domain.booking.dto.CreateEventReq;
import com.bteam.Booking_Beacon.domain.booking.dto.GetEventsRes;
import com.bteam.Booking_Beacon.domain.booking.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("event")
@Tag(name = "Event API")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;

    @GetMapping("info")
    @Operation(summary = "info")
    public void info() {
        log.info("event-service has initialized!");
    }

    @Operation(summary = "event 등록")
    @PostMapping("")
    public ResponseEntity<CreateEventRes> createEvent(@Valid @RequestBody CreateEventReq createEventReq, @RequestAttribute("partnerId") Optional<Long> partnerId) {
        return this.eventService.createEvent(createEventReq, partnerId);
    }

    @Operation(summary = "event 목록 조회")
    @GetMapping("")
    public ResponseEntity<List<GetEventsRes>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return this.eventService.getEvents(page, size);
    }
}
