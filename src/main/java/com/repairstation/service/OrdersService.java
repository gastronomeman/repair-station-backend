package com.repairstation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.repairstation.common.R;
import com.repairstation.domain.dto.OrdersWithStaffNameDto;
import com.repairstation.domain.po.Orders;
import com.repairstation.domain.vo.AOrdersCountVo;
import com.repairstation.domain.vo.OrderTotalVO;
import com.repairstation.domain.vo.OrdersSimpleVO;

import java.util.List;

public interface OrdersService extends IService<Orders> {
    OrderTotalVO getOrderTotal();

    List<OrdersWithStaffNameDto> getOrdersWithStaffNameList(Integer type, Integer status);

    List<OrdersSimpleVO> getOrderSimpleList(String name);

    AOrdersCountVo getAOrdersCount();

    void addOrder(Orders orders);

    void takingOrder(Long id, Orders orders);

    void cancel(Orders orders);

    String checkRepeat(Orders orders);

    void changeSql();
}
