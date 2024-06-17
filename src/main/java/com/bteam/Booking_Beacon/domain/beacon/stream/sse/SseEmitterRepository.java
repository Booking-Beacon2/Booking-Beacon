package com.bteam.Booking_Beacon.domain.beacon.stream.sse;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SseEmitterRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(Long id, SseEmitter emitter) {
        emitters.put(id, emitter);
    }

    public SseEmitter get(Long id) {
        return emitters.get(id);
    }

    public void delete(Long id) {
        emitters.remove(id);
    }
}
