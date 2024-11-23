package com.repairstation;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.repairstation.domain.po.LinkItem;
import com.repairstation.domain.po.WebLink;
import com.repairstation.server.service.StaffService;
import com.repairstation.utils.CSVUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
class RepairStationApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    StaffService staffService;
    @Autowired
    CSVUtils csvUtils;

    @Test
    public void contextLoads1() {
        // 查询所有文档
        List<WebLink> webLinks = mongoTemplate.findAll(WebLink.class);

        for (WebLink webLink : webLinks) {
            for (LinkItem linkItem : webLink.getList()) {
                // 生成 UUID 并设置 id
                if (linkItem.getId() == null || linkItem.getId().isEmpty()) {
                    linkItem.setId(UUID.randomUUID().toString());
                }
            }

            // 更新文档
            Query query = new Query(Criteria.where("title").is(webLink.getTitle()));
            Update update = new Update().set("list", webLink.getList());
            mongoTemplate.updateFirst(query, update, WebLink.class);
        }
    }

    @Test
    public void redis1() {
        LocalDateTime expirationTime = LocalDateTime.of(2024, 11, 23,0,0,0);

        // 使用 Hutool 的 ChineseDate 类获取天干地支
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(expirationTime.toString()));

        // 获取天干地支年份、月份、日期
        String cyclicalYMD = chineseDate.getChineseMonth() + chineseDate.getChineseDay();

        System.out.println(cyclicalYMD);
    }

    @Test
    public void test() {
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

        // 打印随机生成的日期
        System.out.println("随机生成的日期: " + randomDate);

        // 使用 Hutool 的 ChineseDate 类获取天干地支
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(randomDate.toString()));

        // 获取天干地支年份、月份、日期
        String cyclicalYMD = chineseDate.getCyclicalYMD();

        // 去掉年、月、日，只保留天干地支部分
        String[] cyclicalParts = cyclicalYMD.split("[年月日]");
        String cyclicalDate = String.join("", cyclicalParts).trim();

        // 打印去掉年、月、日后的天干地支
        System.out.println("天干地支 (去掉年、月、日): " + cyclicalDate);
    }

}
