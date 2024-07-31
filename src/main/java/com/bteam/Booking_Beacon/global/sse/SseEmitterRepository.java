package com.bteam.Booking_Beacon.global.sse;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Repository
@RequiredArgsConstructor
@Slf4j
public class SseEmitterRepository {

    // 동시성 문제를 해결하기 위해 해당 자료구조 사용
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

    public Integer getEmitterCount() {
        return emitters.size();
    }

}
