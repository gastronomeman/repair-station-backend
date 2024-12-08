package com.rs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.common.R;
import com.rs.domain.dto.OrdersWithStaffNameDto;
import com.rs.domain.po.Orders;
import com.rs.domain.vo.AOrdersCountVo;
import com.rs.domain.vo.OrderTotalVO;
import com.rs.domain.vo.OrdersSimpleVO;

import java.util.List;

public interface OrdersService extends IService<Orders> {
    OrderTotalVO getOrderTotal();

    List<OrdersWithStaffNameDto> getOrdersWithStaffNameList(Integer type, Integer status);

    List<OrdersSimpleVO> getOrderSimpleList(String name);

    AOrdersCountVo getAOrdersCount();

    void addOrder(Orders orders);

    void takingOrder(Long id, Orders orders);

    void cancel(Orders orders);


    void changeSql();
}
