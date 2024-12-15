package com.rs.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rs.domain.po.Staff;
import com.rs.domain.vo.StaffSimpleVO;
import com.rs.enums.PoliticalStatus;
import com.rs.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class StaffCSVUtils {
    @Autowired
    private StaffService staffService;
    @Value("${project-config.csv-path}")
    private String csvPath;

    public String creatStaffCSV() {
        String filePath = csvPath + File.separator + "成员信息.csv";
        File csvFile = new File(filePath);

        // 使用 UTF-8 编码（可以使用 GBK 或其他编码，取决于你的需求）
        CsvWriter writer = CsvUtil.getWriter(csvFile, CharsetUtil.CHARSET_GBK);

        // 写入表头
        writer.write(new String[]{"姓名", "学号", "班级", "政治面貌"}); // 替换为你的列名

        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Staff::getStudentId);
        queryWrapper.ne(Staff::getName, "admin");

        List<Staff> list = staffService.list(queryWrapper);
        // 写入数据
        for (Staff s : list) {
            if (s.getPoliticalStatus() == null) continue;

            if (s.getPoliticalStatus() == PoliticalStatus.CITIZEN) s.setPoliticalStatus(PoliticalStatus.CITIZEN);
            else if (s.getPoliticalStatus() == PoliticalStatus.CPC) s.setPoliticalStatus(PoliticalStatus.CPC);
            else if (s.getPoliticalStatus() == PoliticalStatus.CCYL) s.setPoliticalStatus(PoliticalStatus.CCYL);
            else s.setPoliticalStatus(PoliticalStatus.OTHER);

            writer.write(new String[]{s.getName(), s.getStudentId(), s.getMajor(), s.getPoliticalStatus().getDesc()}); // 替换为实体的字段
        }

        // 关闭写入器
        writer.close();

        return FileUtil.getName(filePath);
    }

    public String creatOrderCount(List<StaffSimpleVO> list, LocalDateTime localDateTime) {
        // 文件路径
        String filePath = csvPath + File.separator + "月度统计.csv";

        // 创建 CSV 写出器
        CsvWriter writer = CsvUtil.getWriter(filePath, CharsetUtil.CHARSET_GBK);

        // 写入表头
        writer.write(new String[]{"姓名", "班级", "学号", "义务劳动日期(月/日)"});

        // 当前日期，从给定的 localDateTime 开始
        LocalDate currentDate = localDateTime.toLocalDate();

        // 存储所有生成的记录
        List<String[]> records = new ArrayList<>();

        // 为每个成员计算其出现的日期
        for (StaffSimpleVO simpleVO : list) {
            int orderCount = simpleVO.getOrderCount();
            for (int i = 0; i < orderCount; i++) {
                // 计算当前成员出现的日期，每人每天最多两次
                LocalDate orderDate = currentDate.plusDays(i / 2);
                records.add(new String[]{
                        simpleVO.getName(),
                        simpleVO.getMajor(),
                        simpleVO.getStudentId(),
                        orderDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                });
            }
        }

        // 按日期排序记录
        records.sort(Comparator.comparing(record -> LocalDate.parse(record[3], DateTimeFormatter.ofPattern("yyyy.MM.dd"))));

        // 将排序后的记录写入 CSV
        for (String[] record : records) {
            writer.write(record);
        }

        // 关闭写出器
        writer.close();
        return FileUtil.getName(filePath);
    }
}
