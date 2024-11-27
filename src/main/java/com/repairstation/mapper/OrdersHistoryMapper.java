package com.repairstation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.repairstation.domain.po.OrdersHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersHistoryMapper extends BaseMapper<OrdersHistory> {
}
