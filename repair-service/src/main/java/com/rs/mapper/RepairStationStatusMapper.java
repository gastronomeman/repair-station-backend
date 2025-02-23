package com.rs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.domain.po.RepairStationStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RepairStationStatusMapper extends BaseMapper<RepairStationStatus> {
    RepairStationStatus getStatusById0();
}
