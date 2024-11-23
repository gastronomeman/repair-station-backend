package com.repairstation.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repairstation.domain.po.Student;
import com.repairstation.server.mapper.StudentMapper;
import com.repairstation.server.service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
