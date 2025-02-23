package com.rs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rs.domain.po.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    void cancelOrders(Orders orders);
}
