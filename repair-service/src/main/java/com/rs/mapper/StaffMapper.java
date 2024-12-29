package com.rs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.domain.po.Staff;
import com.rs.domain.vo.StaffSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StaffMapper extends BaseMapper<Staff> {

    List<StaffSimpleVO> getStaffOrderCounts();

    List<StaffSimpleVO> getStaffOrderList(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
