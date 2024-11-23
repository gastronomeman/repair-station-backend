package com.repairstation.server.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.atomic.AtomicInteger;

@WebListener
@Slf4j
public class StaffOnlineListener implements HttpSessionListener {
    /**
     * 趣味性
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("有人登录了后台 >ᴗ<✧，后台监听，启动！");
    }

}
