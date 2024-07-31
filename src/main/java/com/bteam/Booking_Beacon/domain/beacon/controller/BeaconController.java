package com.bteam.Booking_Beacon.domain.beacon.controller;


import com.bteam.Booking_Beacon.global.sse.SseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("beacon")
@Slf4j
@RequiredArgsConstructor
public class BeaconController {
    private final SseService sseService;

    @GetMapping("info")
    public void info() {
        log.info("BeaconService");
    }

    /**
     * 대기열 관리 API - 조회, 등록
     */
    @PostMapping("wait")
    public void postWait(@RequestAttribute("userId") Long userId) throws JsonProcessingException {
        double dValue = Math.random();
        int randNum = (int)(dValue * 100);
    }

    @GetMapping("wait")
    public void getWait(@RequestAttribute("userId") Long userId) throws JsonProcessingException {
    }

    @GetMapping(value = "sse-subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestAttribute("userId") Long userId) throws JsonProcessingException {
        return this.sseService.subscribe(userId);
    }

}
