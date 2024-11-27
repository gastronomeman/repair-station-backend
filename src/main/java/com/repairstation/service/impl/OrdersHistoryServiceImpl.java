package com.repairstation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repairstation.domain.po.OrdersHistory;
import com.repairstation.mapper.OrdersHistoryMapper;
import com.repairstation.service.OrdersHistoryService;
import org.springframework.stereotype.Service;

@Service
public class OrdersHistoryServiceImpl extends ServiceImpl<OrdersHistoryMapper, OrdersHistory> implements OrdersHistoryService {
}
