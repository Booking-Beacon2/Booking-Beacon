package com.bteam.Booking_Beacon.domain.booking.controller;

import com.bteam.Booking_Beacon.domain.booking.dto.RegisterEventReq;
import com.bteam.Booking_Beacon.domain.booking.dto.RegisterEventRes;
import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import com.bteam.Booking_Beacon.domain.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequiredArgsConstructor
@RequestMapping("booking")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("info")
    public void info() {
        log.info("booking-service has initialized!");
    }

    @PostMapping("events")
    @Operation(summary = "이벤트 등록")
    public ResponseEntity<RegisterEventRes> registerBookingEvents(@RequestBody @Valid RegisterEventReq registerEventReq) {
        return this.bookingService.registerBookingEvents(registerEventReq);
    }

    @GetMapping("musicals")
    @Operation(summary = "뮤지컬 리스트 조회", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MusicalEntity.class)))
            })
    })
    public ResponseEntity<List<MusicalEntity>> getMusicalBookings() {
        return this.bookingService.getMusicalBookings();
    }
}
