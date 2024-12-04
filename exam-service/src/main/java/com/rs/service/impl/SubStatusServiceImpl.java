package com.rs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.domain.po.SubStatus;
import com.rs.mapper.SubStatusMapper;
import com.rs.service.SubStatusService;
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
