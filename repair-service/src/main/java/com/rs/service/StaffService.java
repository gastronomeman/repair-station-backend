package com.rs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.domain.po.Staff;
import com.rs.domain.vo.StaffSimpleVO;

import java.util.List;

public interface StaffService extends IService<Staff> {

    public List<StaffSimpleVO> getStaffOrderCounts();
    public List<StaffSimpleVO> getStaffOrderCountsInTimeRange(String startTime, String endTime);
}
