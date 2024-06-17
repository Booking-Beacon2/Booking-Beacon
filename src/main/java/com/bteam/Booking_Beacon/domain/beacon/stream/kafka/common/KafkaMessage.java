package com.bteam.Booking_Beacon.domain.beacon.stream.kafka.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class KafkaMessage {
    private String topic;
    private Long beaconId;
    private Long userId;

}
