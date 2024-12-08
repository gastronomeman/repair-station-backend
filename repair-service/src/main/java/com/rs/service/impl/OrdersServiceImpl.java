package com.rs.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rs.common.CustomException;
import com.rs.common.R;
import com.rs.domain.dto.OrdersWithStaffNameDto;
import com.rs.domain.po.Orders;
import com.rs.domain.po.OrdersHistory;
import com.rs.domain.po.Staff;
import com.rs.domain.vo.AOrdersCountVo;
import com.rs.domain.vo.OrderTotalVO;
import com.rs.domain.vo.OrdersSimpleVO;
import com.rs.enums.OrderIdentity;
import com.rs.enums.OrderStatus;
import com.rs.mapper.OrdersMapper;
import com.rs.service.OrdersHistoryService;
import com.rs.service.OrdersService;
import com.rs.service.RepairStationStatusService;
import com.rs.service.StaffService;
import com.rs.utils.CountOrdersUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private RepairStationStatusService repairStationStatusService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OrdersHistoryService ordersHistoryService;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public OrderTotalVO getOrderTotal() {
        return CountOrdersUtils.countOrder(this);
    }


    @Override
    public List<OrdersWithStaffNameDto> getOrdersWithStaffNameList(Integer type, Integer status) {
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getStatus, status);
        wrapper.eq(Orders::getOrderType, type);
        wrapper.orderByDesc(Orders::getCreateTime);

        List<Orders> list = list(wrapper);
        List<OrdersWithStaffNameDto> dtos = new ArrayList<>();

        for (Orders orders : list) {
            OrdersWithStaffNameDto dto = new OrdersWithStaffNameDto();
            BeanUtils.copyProperties(orders, dto);
            dtos.add(dto);
        }

        for (OrdersWithStaffNameDto dto : dtos) {
            LambdaQueryWrapper<Staff> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Staff::getId, dto.getStaffId());
            Staff staff = staffService.getOne(wrapper1);
            dto.setStaffName(staff.getName());
        }

        return dtos;
    }

    @Override
    public List<OrdersSimpleVO> getOrderSimpleList(String name) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStudentId, name).or()
                .eq(Orders::getName, name);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        List<Orders> ordersList = this.list(queryWrapper);
        List<OrdersSimpleVO> ordersSimpleVOList = new ArrayList<>();


        for (Orders order : ordersList) {
            OrdersSimpleVO ordersSimpleVO = new OrdersSimpleVO();

            if (order.getStaffId() != null){
                Staff s = staffService.getById(order.getStaffId());
                ordersSimpleVO.setStaffName(s.getName());
                ordersSimpleVO.setStaffPhone(s.getPhone());
            }

            BeanUtils.copyProperties(order, ordersSimpleVO);
            ordersSimpleVOList.add(ordersSimpleVO);
        }



        return ordersSimpleVOList;
    }

    @Override
    public AOrdersCountVo getAOrdersCount() {
        AOrdersCountVo vo = CountOrdersUtils.countAOrders(this, ordersHistoryService, repairStationStatusService);

        LambdaQueryWrapper<Orders> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Orders::getStatus, 1);
        vo.setOrderStatus1((int) this.count(wrapper1));

        LambdaQueryWrapper<Orders> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Orders::getStatus, 2);
        vo.setOrderStatus2((int) this.count(wrapper2));

        LambdaQueryWrapper<Orders> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(Orders::getStatus, 3);
        vo.setOrderStatus3((int) this.count(wrapper3));

        vo.setTotalOrder((int) this.count());

        return vo;
    }

    @Override
    public void addOrder(Orders orders) {
        if (orders.getIdentity() == OrderIdentity.TEACHER) {
            orders.setBuilding("*");
            orders.setStudentId("TEACHER");
        }

        //OrderStatus.WAIT是等待接单
        orders.setStatus(OrderStatus.WAIT);

        this.save(orders);
    }

    @Override
    public void takingOrder(Long id, Orders orders) {
        //没有完善信息不能接单
        Staff s = staffService.getById(id);
        if (!(StrUtil.isNotEmpty(s.getMajor()) && StrUtil.isNotEmpty(s.getPhone())))
            throw new CustomException("请完善个人信息后再接单");
        else if (s.getPoliticalStatus() == null)
            throw new CustomException("请完善个人信息后再接单");


        if (this.getById(orders.getId()).getStatus() == OrderStatus.REPAIR)
            throw new CustomException("已有人接单，请刷新重试");

        orders.setStaffId(id);
        orders.setStatus(OrderStatus.REPAIR);

        this.updateById(orders);

    }

    @Override
    public void cancel(Orders orders) {
        orders.setCompletionTime(LocalDateTime.now());
        ordersMapper.cancelOrders(orders);  // 执行更新
    }

    @Override
    public String checkRepeat(Orders orders) {
        if (StrUtil.isBlank(orders.getStudentId())) return null;

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getStudentId, orders.getStudentId())
                .and(w -> w.eq(Orders::getStatus, 1).or().eq(Orders::getStatus, 2));

        long count = this.count(wrapper);
        if (count > 0) return orders.getStudentId();

        return null;
    }

    @Override
    public void changeSql() {
        List<Orders> list = list();

        List<OrdersHistory> ordersHistoryList = new ArrayList<>();
        for (Orders orders : list) {
            OrdersHistory ordersHistory = new OrdersHistory();
            BeanUtils.copyProperties(orders, ordersHistory);
            ordersHistoryList.add(ordersHistory);
        }

        ordersHistoryService.saveBatch(ordersHistoryList);

        remove(null);
    }


}
