package com.repairstation.utils;

import com.repairstation.domain.vo.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtils {
    public static Boolean saveStaff(RedisTemplate<Object, Object> redisTemplate, String jwt, Map<String, Object> map) {
        String key = "jwt::" + jwt;

        //如果jwt已存在则
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        if (!keys.isEmpty()) {
            redisTemplate.expire(key, 2, TimeUnit.MINUTES);
            return true;
        }

        //不存在则根据id检查是否已有设备登录
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        Set<Object> jwtKeys = redisTemplate.keys("jwt*");

        if (!(jwtKeys == null || jwtKeys.isEmpty())) {
            for (Object jwtKey : jwtKeys) {
                String id = (String) hashOperations.get(jwtKey, "id");
                //检查到已有登录账号
                if (id != null
                        && id.equals(JWTUtils.getIdByJwt(jwt))
                        && !key.equals(jwtKey))
                    return false;
            }
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String hashKey = entry.getKey();
            Object value = entry.getValue();
            hashOperations.put(key, hashKey, value);
        }

        //设置2分钟过期
        redisTemplate.expire(key, 2, TimeUnit.MINUTES);

        return true;
    }

    public static void exitStaff(RedisTemplate<Object, Object> redisTemplate, String jwt) {
        String key = "jwt::" + jwt;
        redisTemplate.delete(key);
    }

    public static int countOnlineStaff(RedisTemplate<Object, Object> redisTemplate) {
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();

        Set<Object> keys = redisTemplate.keys("jwt*");
        if (keys == null || keys.isEmpty()) return 0;

        int size = keys.size();

        //忽略admin账号
        for (Object key : keys) {
            String nameValue = (String) hashOperations.get(key, "name");
            if (nameValue != null && nameValue.equals("admin")) {
                size--;
                break;
            }
        }

        return size;
    }

    public static List<String> onlineStaffName(RedisTemplate<Object, Object> redisTemplate) {
        HashOperations<Object, Object, Object> hashOperations = redisTemplate.opsForHash();
        Set<Object> keys = redisTemplate.keys("jwt*");

        if (keys == null || keys.isEmpty()) return null;

        List<String> list = new ArrayList<>();
        for (Object key : keys) {
            String nameValue = (String) hashOperations.get(key, "name");

            //忽略admin账号
            if (nameValue != null && nameValue.equals("admin")) continue;
            list.add(nameValue);
        }

        return list;
    }

    public static void removeOrderList(RedisTemplate<Object, Object> redisTemplate) {
        String pattern1 = "orderList1::*"; // 设置模式
        Set<Object> keys1 = redisTemplate.keys(pattern1); // 获取匹配的键
        if (keys1 != null && !keys1.isEmpty()) {
            redisTemplate.delete(keys1); // 删除所有匹配的键
        }

        String pattern2 = "orderList2::*"; // 设置模式
        Set<Object> keys2 = redisTemplate.keys(pattern2); // 获取匹配的键
        if (keys2 != null && !keys2.isEmpty()) {
            redisTemplate.delete(keys2); // 删除所有匹配的键
        }
    }

    public static void removeAllTotal(RedisTemplate<Object, Object> redisTemplate) {
        removeOrderList(redisTemplate);

        redisTemplate.delete("admin::total");
        redisTemplate.delete("order::total");
        redisTemplate.delete("leaderboardCache::0");
        redisTemplate.delete("commonCache::check-dir");

    }

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