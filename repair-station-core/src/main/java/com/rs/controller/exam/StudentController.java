package com.rs.controller.exam;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rs.common.R;
import com.rs.domain.dto.StudentDto;
import com.rs.domain.po.Student;
import com.rs.domain.vo.VerificationCode;
import com.rs.service.StudentService;
import com.rs.utils.DownloadUtil;
import com.rs.utils.EXRedisUtils;
import com.rs.utils.ScheduledUtils;
import com.rs.utils.StudentCSVUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/stu")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StudentCSVUtils studentCsvUtils;

    @Value("${project-config.csv-path}")
    private String csvPath;

    @PostMapping
    public R<Student> addStudent(@RequestBody Student student) {
        Student s = studentService.getById(student.getId());
        if (s != null) return R.success(s);

        studentService.save(student);
        return R.success(student);
    }

    @PostMapping("/check-code")
    public R<String> checkCode(@RequestBody StudentDto dto) {
        // 尝试从 Redis 获取验证码
        VerificationCode existingCode = EXRedisUtils.getCodeFromRedis(redisTemplate);

        if (existingCode == null) return R.error("获取验证码失败，请重新尝试");
        if (!existingCode.getCode().equals(dto.getCode())) return R.error("获取验证码失败，请重新尝试");

        Student s = new Student();
        BeanUtils.copyProperties(dto, s);
        studentService.updateById(s);

        return R.success("录入成功！");
    }

    @GetMapping("/page")
    public R<Page<Student>> page(int page, int pageSize, String name) {
        Page<Student> studentPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        if (StrUtil.isNotEmpty(name)) {
            queryWrapper.like(Student::getName, name)
                    .or().like(Student::getId, name);
        }

        queryWrapper.orderByDesc(Student::getCreateTime);

        studentService.page(studentPage, queryWrapper);

        return R.success(studentPage);
    }

    @GetMapping("/csv")
    public void getStudentCsv(HttpServletResponse response) {
        String s = studentCsvUtils.creatStudentCSV();
        s = csvPath + File.separator + s;
        ScheduledUtils.delFile10Min(s);
        DownloadUtil.download(response, s, FileUtil.getName(s));
    }
}
