package com.rs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.domain.po.Sub;

import java.util.List;

public interface SubService extends IService<Sub> {
    List<Sub> getRandomSubs(String limit);
}
