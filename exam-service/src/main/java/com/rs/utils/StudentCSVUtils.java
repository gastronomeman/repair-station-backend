package com.rs.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rs.domain.po.Student;
import com.rs.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@Slf4j
public class StudentCSVUtils {
    @Autowired
    StudentService studentService;

    @Value("${project-config.csv-path}")
    private String csvPath;

    public String creatStudentCSV() {
        String filePath = csvPath + File.separator + "学生信息.csv";
        File csvFile = new File(filePath);

        // 使用 UTF-8 编码（可以使用 GBK 或其他编码，取决于你的需求）
        CsvWriter writer = CsvUtil.getWriter(csvFile, CharsetUtil.CHARSET_GBK);

        // 写入表头
        writer.write(new String[]{"姓名", "学号", "学院", "班级", "答题时间", "创建时间", "更新时间"}); // 替换为你的列名

        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Student::getCreateTime);

        List<Student> list = studentService.list(queryWrapper);

        // 写入数据
        for (Student s : list) {
            if (s.getScore() == null) s.setScore("成绩无效");

            writer.write(new String[]{
                    String.valueOf(s.getId()),
                    s.getName(),
                    s.getCollege(),
                    s.getClassId(),
                    s.getScore(),
                    String.valueOf(s.getCreateTime()),
                    String.valueOf(s.getUpdateTime()),
            });
        }

        // 关闭写入器
        writer.close();

        return FileUtil.getName(filePath);
    }

}
