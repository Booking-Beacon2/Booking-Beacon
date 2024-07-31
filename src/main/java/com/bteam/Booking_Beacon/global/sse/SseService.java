package com.bteam.Booking_Beacon.global.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SseService {
    private static final long TIMEOUT = 60 * 1000;
    private static final long RECONNECTION_TIMEOUT = 1000L;
    private final SseEmitterRepository emitterRepository;

    /**
     *
     * @param userId 구독하려는 client userId
     * @description client 가 구독하기
     */
    public SseEmitter subscribe(Long userId) {
        /**
         * client 와 server 가 연결하기 위해 emitter 생성
         */
        SseEmitter emitter = this.createEmitter(userId);

        this.notify(userId, "subscribed!!!");
        return emitter;
    }


    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitterRepository.save(userId, emitter);

        // 완료가 되거나 타임아웃이 발생하면 repository 에서 삭제하는 그러한 emitter 를 생성
        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        return emitter;
    }

    /**
     *
     * @param userId 메세지 받는 유저
     * @param message 보낼 메세지
     * @description emitter 연결된 client 에게 메세지 보내기
     */
    private void publishEmitter(Long userId, String message) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(userId)).name("sse").data(message));
                log.info("send to emitter {}", message);
            } catch (IOException e) {
                emitterRepository.delete(userId);
                emitter.completeWithError(e);
            }
        }
    }

    public void notify(Long userId, String message) {
        this.publishEmitter(userId, message);
    }

    // 전체 emitter 리스트
    public Map<Long, SseEmitter> getEmitters() {
        return this.emitterRepository.getEmitters();
    }


    // 전체 emitter 개수
    public Integer getEmitterCount() {
        return this.emitterRepository.getEmitterCount();
    }

}
