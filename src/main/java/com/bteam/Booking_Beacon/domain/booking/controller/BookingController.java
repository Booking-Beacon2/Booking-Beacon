package com.bteam.Booking_Beacon.domain.booking.controller;

import com.bteam.Booking_Beacon.domain.booking.dto.RegisterMusicalReq;
import com.bteam.Booking_Beacon.domain.booking.dto.RegisterMusicalRes;
import com.bteam.Booking_Beacon.domain.booking.entity.MusicalEntity;
import com.bteam.Booking_Beacon.domain.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping("musicals")
    @Operation(summary = "뮤지컬 등록")
    public ResponseEntity<RegisterMusicalRes> registerMusicalBooking(@RequestBody @Validated RegisterMusicalReq registerMusicalReq) {
        Long now = System.currentTimeMillis();
        log.info(now + ": registering MusicalBooking");

        return this.bookingService.registerMusicalBooking(registerMusicalReq);
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
