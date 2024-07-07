package com.bteam.Booking_Beacon.global.util;

import com.bteam.Booking_Beacon.global.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
}
