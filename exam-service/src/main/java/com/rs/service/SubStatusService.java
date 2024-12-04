package com.rs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.domain.po.SubStatus;

public interface SubStatusService extends IService<SubStatus> {
    SubStatus getSubStatus();
}
