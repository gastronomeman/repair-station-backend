package com.rs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.domain.po.OrdersHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersHistoryMapper extends BaseMapper<OrdersHistory> {
}
