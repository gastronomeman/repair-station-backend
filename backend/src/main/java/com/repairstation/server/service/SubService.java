package com.repairstation.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repairstation.domain.po.Sub;

import java.util.List;

public interface SubService extends IService<Sub> {
    List<Sub> getRandomSubs(String limit);
}
