package com.repairstation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repairstation.domain.po.RepairStationStatus;
import com.repairstation.mapper.RepairStationStatusMapper;
import com.repairstation.service.RepairStationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepairStationStatusServiceImpl extends ServiceImpl<RepairStationStatusMapper, RepairStationStatus>
        implements RepairStationStatusService {

    @Autowired
    private RepairStationStatusMapper repairStationStatusMapper;

    @Override
    public RepairStationStatus getStatus() {
        return repairStationStatusMapper.getStatusById0();
    }
}
