package com.repairstation.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repairstation.domain.po.SubStatus;

public interface SubStatusService extends IService<SubStatus> {
    SubStatus getSubStatus();
}
