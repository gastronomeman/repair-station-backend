package com.repairstation.interceptor;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import com.repairstation.common.R;
import com.repairstation.utils.JWTUtils;
import com.repairstation.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//自定义拦截器
@Component // 当前拦截器对象由Spring创建和管理
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.判断是 OPTIONS 请求，放行
        if (request.getMethod().equals("OPTIONS")) return true;

        //2.获取请求url
        log.info("♪（＾∀＾●）ﾉｼ （●´∀｀）♪收到的请求是：{}", request.getRequestURI());


        //3.获取请求头中的令牌（token）
        String token = request.getHeader("Authorization");
        log.info("收到token令牌哦ヾ(^▽^*)))");

        //4.判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if (StringUtils.isBlank(token)) {
            log.info("令牌不存在！");
            //发送未登录响应给前端
            sendNotLogin(response);
            //不放行
            return false;
        }

        //5.解析token，如果解析失败，返回错误结果（未登录），如果成功有会话就不创建会话了
        try {
            JWTPayload jwtPayload = JWTUtils.parseJWT(token);
            //获取标题直接跳过，避免重复添加项目

            Map<String, Object> map = new HashMap<>();
            map.put("id", jwtPayload.getClaim("id"));
            map.put("studentId", jwtPayload.getClaim("studentId"));
            map.put("name", jwtPayload.getClaim("name"));

            //检查是否重复登录
            if(!RedisUtils.saveStaff(redisTemplate, token, map)){
                sendOntOnly(response);
                return false;
            }

        } catch (Exception e) {
            log.info("令牌解析失败!{}", e.getMessage());
            //发送未登录响应给前端
            sendNotLogin(response);
            //不放行
            return false;
        }

        //6.放行
        return true;
    }

    private void sendNotLogin(HttpServletResponse response) throws IOException {
        //创建响应结果对象
        R<Object> responseResult = R.error("not_login");
        //把R对象转换为JSON格式字符串
        String json = JSONUtil.toJsonStr(responseResult);
        //设置响应头（告知浏览器：响应的数据类型为json、响应的数据编码表为utf-8）
        response.setContentType("application/json;charset=utf-8");
        //响应
        response.getWriter().write(json);
    }

    private void sendOntOnly(HttpServletResponse response) throws IOException {
        //创建响应结果对象
        R<Object> responseResult = R.error("检测到账号已在别的设备登录<br />请退出登录后重新尝试<br />╮(๑•́ ₃•̀๑)╭");
        //把R对象转换为JSON格式字符串
        String json = JSONUtil.toJsonStr(responseResult);
        //设置响应头（告知浏览器：响应的数据类型为json、响应的数据编码表为utf-8）
        response.setContentType("application/json;charset=utf-8");
        //响应
        response.getWriter().write(json);
    }
}
