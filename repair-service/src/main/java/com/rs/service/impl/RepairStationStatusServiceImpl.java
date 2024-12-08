package com.rs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.domain.po.RepairStationStatus;
import com.rs.mapper.RepairStationStatusMapper;
import com.rs.service.RepairStationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RepairStationStatusServiceImpl extends ServiceImpl<RepairStationStatusMapper, RepairStationStatus>
        implements RepairStationStatusService {

    @Autowired
    private RepairStationStatusMapper repairStationStatusMapper;

    @Override
    public RepairStationStatus getStatus() {
        return repairStationStatusMapper.getStatusById0();
    }
}
