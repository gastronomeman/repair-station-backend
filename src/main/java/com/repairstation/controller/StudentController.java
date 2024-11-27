package com.repairstation.controller;

import com.repairstation.common.R;
import com.repairstation.domain.dto.StudentDto;
import com.repairstation.domain.po.Student;
import com.repairstation.domain.vo.VerificationCode;
import com.repairstation.service.StudentService;
import com.repairstation.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stu")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

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
        VerificationCode existingCode = RedisUtils.getCodeFromRedis(redisTemplate);

        if (existingCode == null) return R.error("获取验证码失败，请重新尝试");
        if (!existingCode.getCode().equals(dto.getCode())) return R.error("获取验证码失败，请重新尝试");

        Student s = new Student();
        BeanUtils.copyProperties(dto, s);
        studentService.updateById(s);

        return R.success("录入成功！");
    }
}
