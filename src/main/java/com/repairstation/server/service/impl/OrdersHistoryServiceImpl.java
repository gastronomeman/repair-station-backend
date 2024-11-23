package com.repairstation.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.repairstation.domain.po.OrdersHistory;
import com.repairstation.server.mapper.OrdersHistoryMapper;
import com.repairstation.server.service.OrdersHistoryService;
import org.springframework.stereotype.Service;

@Service
public class OrdersHistoryServiceImpl extends ServiceImpl<OrdersHistoryMapper, OrdersHistory> implements OrdersHistoryService {
}
