//package com.bteam.Booking_Beacon.domain.beacon.controller;
//
//
//import com.bteam.Booking_Beacon.domain.beacon.dto.PickReq;
//import com.bteam.Booking_Beacon.domain.beacon.stream.kafka.producer.KafkaProducer;
//import com.bteam.Booking_Beacon.domain.beacon.stream.redis.service.RedisService;
//import com.bteam.Booking_Beacon.domain.beacon.stream.sse.SseService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@RestController
//@RequestMapping("beacon")
//@Slf4j
//@RequiredArgsConstructor
//public class BeaconController {
//    private final KafkaProducer kafkaProducer;
//    private final RedisService redisService;
//    private final SseService sseService;
//
//    @GetMapping("info")
//    public void info() {
//        log.info("BeaconService");
//    }
//
//    @PostMapping("pick")
//    public void pick(@RequestBody PickReq pickReq, @RequestAttribute("userId") Long userId) throws JsonProcessingException {
//        this.kafkaProducer.sendMessage(pickReq, userId);
//    }
//
//    /**
//     * 대기열 관리 API - 조회, 등록
//     */
//    @PostMapping("wait")
//    public void postWait(@RequestAttribute("userId") Long userId) throws JsonProcessingException {
//        double dValue = Math.random();
//        int randNum = (int)(dValue * 100);
//        this.redisService.setRedis(userId.toString(), Integer.toString(randNum));
//    }
//
//    @GetMapping("wait")
//    public void getWait(@RequestAttribute("userId") Long userId) throws JsonProcessingException {
//        this.redisService.getRedis(userId.toString());
//    }
//
//    @GetMapping(value = "sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribe(@RequestHeader("userId") Long userId) throws JsonProcessingException {
//        return this.sseService.subscribe(userId);
//    }
//
//}
