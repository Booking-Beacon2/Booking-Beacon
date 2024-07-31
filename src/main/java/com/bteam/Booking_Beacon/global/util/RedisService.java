package com.bteam.Booking_Beacon.global.util;

import com.bteam.Booking_Beacon.global.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    @Autowired private RedisConfig redisConfig;

    public void setValue(String key, String value, int time, TimeUnit timeUnit) {
        ValueOperations<String, String> valueOperation = redisConfig.redisTemplate().opsForValue();
        valueOperation.set(key, value, time, timeUnit);
    }

    public Object getValue(String key) {
        return redisConfig.redisTemplate().opsForValue().get(key);
    }

    // Zset : 대기열에 특화된 자료구조
    public void setRedis(String key, String value) {
        ZSetOperations<String, String> zSetOperations = redisConfig.redisTemplate().opsForZSet();
        Date date = new Date();
        double score = date.getTime();
        zSetOperations.add(key, value, score);

    }

    public void getRedis(String key) {
        ZSetOperations<String, String> zSetOperations = redisConfig.redisTemplate().opsForZSet();
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
