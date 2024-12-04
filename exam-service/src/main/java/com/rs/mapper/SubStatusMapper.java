package com.rs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.domain.po.SubStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubStatusMapper extends BaseMapper<SubStatus> {

    @Select("select * from sub_status where id = '0' ")
    SubStatus getStatusById0();
}
