package com.repairstation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repairstation.domain.po.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
