package com.rs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.domain.po.OrdersHistory;
import com.rs.mapper.OrdersHistoryMapper;
import com.rs.service.OrdersHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdersHistoryServiceImpl extends ServiceImpl<OrdersHistoryMapper, OrdersHistory> implements OrdersHistoryService {
}
