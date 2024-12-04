package com.rs.utils;

import com.rs.domain.vo.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class EXRedisUtils {
    public static VerificationCode setCodeWithExpiration(RedisTemplate<Object, Object> redisTemplate, String code) {
        //设置70s过期
        int s = 70;

        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(s);

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("time", expirationTime);

        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String hashKey = entry.getKey();
            Object value = entry.getValue();
            hashOperations.put("code", hashKey, value);
        }

        //设置70s过期
        redisTemplate.expire("code", s, TimeUnit.SECONDS);

        VerificationCode v = new VerificationCode();
        v.setCode(code);
        v.setTime(String.valueOf(expirationTime.atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli()));

        return v;
    }

    public static VerificationCode getCodeFromRedis(RedisTemplate<Object, Object> redisTemplate) {
        // 尝试从 Redis 获取验证码
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        String code = (String) hashOperations.get("code", "code");
        LocalDateTime expirationTime = (LocalDateTime) hashOperations.get("code", "time");


        if (code != null && expirationTime != null) {
            VerificationCode v = new VerificationCode();
            v.setCode(code);
            v.setTime(String.valueOf(expirationTime.atZone(ZoneOffset.ofHours(8)).toInstant().toEpochMilli()));
            return v;
        }

        return null; // 如果 Redis 中没有验证码，则返回 null
    }
}
