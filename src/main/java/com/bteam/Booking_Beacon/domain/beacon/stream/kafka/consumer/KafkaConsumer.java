//package com.bteam.Booking_Beacon.domain.beacon.stream.kafka.consumer;
//
//import com.bteam.Booking_Beacon.domain.beacon.dto.PickReq;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class KafkaConsumer {
//
//    private final ObjectMapper objectMapper;
//
//    @KafkaListener(topics = "topic1", groupId = "group1")
//    public void listen1(String message) throws JsonProcessingException {
//
//        PickReq pickReq = objectMapper.readValue(message, PickReq.class);
//        log.info("1111>>>>>>>>>");
//        log.info(String.valueOf(pickReq.getBeaconId()));
//    }
//
//    @KafkaListener(topics = "topic2", groupId = "group1")
//    public void listen2(String message) throws JsonProcessingException {
//
//        PickReq pickReq = objectMapper.readValue(message, PickReq.class);
//        log.info("2222>>>>>>>>>");
//        log.info(String.valueOf(pickReq.getBeaconId()));
//    }
//}
//
//
//package com.bteam.Booking_Beacon.domain.beacon.stream.kafka.consumer;
//
//import com.bteam.Booking_Beacon.domain.beacon.dto.PickReq;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class KafkaConsumer {
//
//    private final ObjectMapper objectMapper;
//
//    @KafkaListener(topics = "topic1", groupId = "group1")
//    public void listen1(String message) throws JsonProcessingException {
//
//        PickReq pickReq = objectMapper.readValue(message, PickReq.class);
//        log.info("1111>>>>>>>>>");
//        log.info(String.valueOf(pickReq.getBeaconId()));
//    }
//
//    @KafkaListener(topics = "topic2", groupId = "group1")
//    public void listen2(String message) throws JsonProcessingException {
//
//        PickReq pickReq = objectMapper.readValue(message, PickReq.class);
//        log.info("2222>>>>>>>>>");
//        log.info(String.valueOf(pickReq.getBeaconId()));
//    }
//}
//
//
