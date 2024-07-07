package com.bteam.Booking_Beacon.domain.beacon.stream.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeaconRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * @param key   userId.toString()
     * @param value user 가 선택한 랜덤 숫자
     * @description 처음이라면 저장하고 기존에 있다면 해당 자리 숫자 업데이트
     */
    public void setRedis(String key, String value) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Date date = new Date();
        double score = date.getTime();
        zSetOperations.add(key, value, score);

    }

    public void getRedis(String key) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        try {
            ZSetOperations.TypedTuple<String> zSet = Objects.requireNonNull(zSetOperations.popMin(key));
            String value = zSet.getValue();
            Double score = zSet.getScore();
            assert value != null;

            log.info("value {} score {}", value, score);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("empty queue");
        }
    }


}
