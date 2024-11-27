package com.repairstation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repairstation.domain.po.RepairStationStatus;

public interface RepairStationStatusService extends IService<RepairStationStatus> {
    RepairStationStatus getStatus();
}
