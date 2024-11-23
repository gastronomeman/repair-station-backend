package com.repairstation.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repairstation.domain.po.Sub;
import com.repairstation.server.mapper.SubMapper;
import com.repairstation.server.service.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServiceImpl extends ServiceImpl<SubMapper, Sub> implements SubService {
    @Autowired
    private SubMapper subMapper;

    @Override
    public List<Sub> getRandomSubs(String limit) {
        QueryWrapper<Sub> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("ORDER BY RAND() LIMIT " + limit); // 使用 .last 来追加 LIMIT 和 ORDER BY
        return subMapper.selectList(queryWrapper); // 使用 MyBatis-Plus 查询
    }
}
