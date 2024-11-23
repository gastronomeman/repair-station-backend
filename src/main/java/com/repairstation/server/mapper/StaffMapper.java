package com.repairstation.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repairstation.domain.po.Staff;
import com.repairstation.domain.vo.StaffSimpleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StaffMapper extends BaseMapper<Staff> {

    @Select("SELECT s.name AS name, COUNT(o.id) AS orderCount " +
            "FROM staff s " +
            "LEFT JOIN orders o ON s.id = o.staff_id " +
            "WHERE o.status = 3 " +
            "GROUP BY s.name " +
            "ORDER BY orderCount DESC")
    List<StaffSimpleVO> getStaffOrderCounts();

    @Select("SELECT s.id AS id, s.name AS name, s.student_id AS studentId, s.major AS major, " +
            "COUNT(o.id) AS orderCount " +
            "FROM staff s " +
            "LEFT JOIN orders o ON s.id = o.staff_id " +
            "WHERE o.status = 3 " +
            "AND o.completion_time BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY s.id, s.name " +
            "ORDER BY orderCount DESC")
    List<StaffSimpleVO> getStaffOrderList(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
