package com.rs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.domain.po.RepairStationStatus;

public interface RepairStationStatusService extends IService<RepairStationStatus> {
    RepairStationStatus getStatus();
}
