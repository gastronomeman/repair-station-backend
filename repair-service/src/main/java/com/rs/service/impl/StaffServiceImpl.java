package com.rs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.domain.po.Staff;
import com.rs.domain.vo.StaffSimpleVO;
import com.rs.mapper.StaffMapper;
import com.rs.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {
    @Autowired
    private StaffMapper staffMapper;


    @Override
    public List<StaffSimpleVO> getStaffOrderCounts() {
        return staffMapper.getStaffOrderCounts();
    }

    @Override
    public List<StaffSimpleVO> getStaffOrderCountsInTimeRange(String startTime, String endTime) {
        if (startTime == null || startTime.isEmpty()) {
            startTime = "2000-01-01 00:00:00"; // 默认值
        }
        if (endTime == null || endTime.isEmpty()) {
            endTime = "2999-12-31 23:59:59"; // 默认值
        }
        return staffMapper.getStaffOrderList(startTime, endTime);
    }
}
