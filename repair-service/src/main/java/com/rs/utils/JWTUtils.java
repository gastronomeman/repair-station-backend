package com.rs.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;

import com.rs.common.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JWTUtils {
    private static final Long EXPIRE_TIME = 259200000L; // 令牌有效时间3天

    /**
     * 对消息封装成jwt令牌
     *
     * @param claims 要设置的键值对数据
     * @param salt   需要添加的盐
     * @return
     */
    public static String generateJwt(Map<String, Object> claims, String salt) {
        String SIGNING_KEY = "祝愿ITeam基地维修站越办越好！✧*｡ (ˊᗜˋ*) ✧*！" + salt;
        SIGNING_KEY += UUID.randomUUID().toString().replace("-", "");
        // 获取当前时间
        Date now = new Date();
        // 计算过期时间
        Date expiration = new Date(now.getTime() + EXPIRE_TIME);

        // 设置JWT的负载信息
        claims.put("exp", expiration);

        return JWTUtil.createToken(claims, SIGNING_KEY.getBytes());
    }

    /**
     * 解析jwt令牌
     *
     * @param jwt 传入的jwt数据
     * @return
     */
    public static JWTPayload parseJWT(String jwt) {
        // 使用JWTUtil.parseToken方法解析JWT令牌
        JWT jwtObj = JWTUtil.parseToken(jwt);

        // 获取过期时间
        Date expiration = jwtObj.getPayload().getClaimsJson().getDate("exp");

        // 如果没有过期时间，认为令牌不正常
        if (expiration == null) throw new CustomException("令牌不能不带时间！");

        try {
            JWTValidator.of(jwt).validateDate(DateUtil.date());
        } catch (ValidateException e) {
            throw new CustomException("令牌超时！");
        }

        // 返回JWT令牌的内容（负载）
        return jwtObj.getPayload();
    }


    public static String getIdByRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return getIdByJwt(token);
    }

    public static String getIdByJwt(String jwt) {
        JWTPayload jwtPayload = parseJWT(jwt);
        return (String) jwtPayload.getClaim("id");
    }
}
