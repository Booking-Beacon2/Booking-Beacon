package com.bteam.Booking_Beacon.global.schedule;

import com.bteam.Booking_Beacon.global.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class StatusSchedule {

    private final SseService sseService;
    private static final Integer MAX_WAITERS = 10;
    // 1초 마다
//    @Scheduled(cron = "*/10 * * * * *")
    public void schedule() {

        Integer waitUsers = this.sseService.getEmitterCount();
        if (waitUsers < MAX_WAITERS) {
            /**
             * 1. 대기열에서 예약 차례인 유저 찾는다
             * 2. notify(userId, "예약 가능합니다")
             *
             */
        }
        log.info("waitUsers -> {}", waitUsers.toString());
    }
}
