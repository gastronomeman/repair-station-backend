package com.rs.utils;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class RandomDateUtils {
    public static String RandomChineseDateExample() {
        // 生成 1949 年到 现在 之间的随机日期
        LocalDate startDate = LocalDate.of(1949, 10, 1);  // 开始日期
        LocalDate endDate = LocalDate.now();  // 结束日期


        // 将起始日期和结束日期转为毫秒值（东八区）
        long startMillis = startDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
        long endMillis = endDate.atStartOfDay(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();

        // 随机生成一个时间戳
        long randomMillis = RandomUtil.randomLong(startMillis, endMillis + 1);

        // 将时间戳转换为 ZonedDateTime（东八区），然后获取 LocalDate
        LocalDate randomDate = Instant.ofEpochMilli(randomMillis)
                .atZone(ZoneId.of("Asia/Shanghai"))
                .toLocalDate();

        // 使用 Hutool 的 ChineseDate 类获取天干地支
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(randomDate.toString()));

        // 获取天干地支年份、月份、日期
        String cyclicalYMD = chineseDate.getCyclicalYMD();

        // 去掉年、月、日，只保留天干地支部分
        String[] cyclicalParts = cyclicalYMD.split("[年月日]");
        // 打印去掉年、月、日后的天干地支
        return String.join("", cyclicalParts).trim().substring(2);
    }
}
