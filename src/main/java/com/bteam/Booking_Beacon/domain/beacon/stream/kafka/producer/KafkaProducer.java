package com.bteam.Booking_Beacon.domain.beacon.stream.kafka.producer;

import com.bteam.Booking_Beacon.domain.beacon.dto.PickReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final static List<String> topicList = List.of("topic1", "topic2");
    private final KafkaTemplate<String, String> kafkaProducerTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(PickReq pickReq, Long userId) throws JsonProcessingException {
        log.info("Sending pick request to topic: {} and userId: {}", pickReq, userId);
        String randomTopic = topicList.get(new Random().nextInt(topicList.size()));
        String message = objectMapper.writeValueAsString(pickReq);

        this.kafkaProducerTemplate.send(randomTopic, message);
    }
}
