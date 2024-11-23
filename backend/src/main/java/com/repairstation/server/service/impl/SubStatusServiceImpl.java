package com.repairstation.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repairstation.domain.po.SubStatus;
import com.repairstation.server.mapper.SubStatusMapper;
import com.repairstation.server.service.SubStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubStatusServiceImpl extends ServiceImpl<SubStatusMapper, SubStatus> implements SubStatusService {
    @Autowired
    private SubStatusMapper subStatusMapper;

    @Override
    public SubStatus getSubStatus() {
        return subStatusMapper.getStatusById0();
    }
}
