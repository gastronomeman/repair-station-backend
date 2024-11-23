package com.repairstation.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ScheduledUtils {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanRedis() {
        redisTemplate.delete("order::total");
        redisTemplate.delete("admin::total");
        log.info("执行清除任务！");
    }

    public static void delFile1Hour(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
         //计划在2h后删除文件
        scheduler.schedule(() -> {
            FileUtil.del(filePath);
            log.info("2h删除文件：{}", filePath);
        }, 2, TimeUnit.HOURS);
    }
    public static void delFile10Min(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //计划在10分钟后删除文件
        scheduler.schedule(() -> {
            FileUtil.del(filePath);
            log.info("10min删除文件：{}", filePath);
        }, 10, TimeUnit.MINUTES);
    }
}
