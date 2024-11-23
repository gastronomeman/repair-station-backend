package com.repairstation.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repairstation.domain.po.SubStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubStatusMapper extends BaseMapper<SubStatus> {

    @Select("select * from sub_status where id = '0' ")
    SubStatus getStatusById0();
}
