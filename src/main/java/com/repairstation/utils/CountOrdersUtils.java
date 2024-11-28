package com.repairstation.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.repairstation.domain.po.Orders;
import com.repairstation.domain.po.OrdersHistory;
import com.repairstation.domain.po.RepairStationStatus;
import com.repairstation.domain.vo.AOrdersCountVo;
import com.repairstation.domain.vo.OrderTotalVO;
import com.repairstation.enums.ServerStatus;
import com.repairstation.service.OrdersHistoryService;
import com.repairstation.service.OrdersService;
import com.repairstation.service.RepairStationStatusService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class CountOrdersUtils {
    private static Integer countOrdersByDateRange(OrdersService ordersService, LocalDateTime start, LocalDateTime end) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 3);
        queryWrapper.between(Orders::getCompletionTime, start, end);
        return Math.toIntExact(ordersService.count(queryWrapper));
    }

    private static Integer countSemesterOrder(OrdersService ordersService) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 3);
        return Math.toIntExact(ordersService.count(queryWrapper));
    }

    private static Integer countLastMonthOrder(OrdersService ordersService) {
        LocalDateTime start = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().withDayOfMonth(1).minusDays(1).with(LocalTime.MAX);
        return countOrdersByDateRange(ordersService, start, end);
    }

    private static Integer countThisMonthOrder(OrdersService ordersService) {
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return countOrdersByDateRange(ordersService, start, end);
    }

    private static Integer countOrdersThisWeek(OrdersService ordersService) {
        LocalDateTime start = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
        return countOrdersByDateRange(ordersService, start, end);
    }

    private static Integer countOrdersYesterday(OrdersService ordersService) {
        LocalDateTime start = LocalDateTime.now().minusDays(1).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().minusDays(1).with(LocalTime.MAX);
        return countOrdersByDateRange(ordersService, start, end);
    }

    private static Integer countOrdersToday(OrdersService ordersService) {
        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        return countOrdersByDateRange(ordersService, start, end);
    }

    private static Integer countOrdersByTypeAndStatus(OrdersService ordersService, Integer type, Integer status) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderType, type);
        queryWrapper.eq(Orders::getStatus, status);
        return Math.toIntExact(ordersService.count(queryWrapper));
    }

    private static Integer countOrdersHistoryByTypeAndStatus(OrdersHistoryService ordersService, Integer type, Integer status) {
        LambdaQueryWrapper<OrdersHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrdersHistory::getOrderType, type);
        queryWrapper.eq(OrdersHistory::getStatus, status);
        return Math.toIntExact(ordersService.count(queryWrapper));
    }

    private static Integer countOrdersType11(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 1, 1);
    }

    private static Integer countOrdersType12(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 1, 2);
    }

    private static Integer countOrdersType13(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 1, 3);
    }

    private static Integer countOrdersType21(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 2, 1);
    }

    private static Integer countOrdersType22(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 2, 2);
    }

    private static Integer countOrdersType23(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 2, 3);
    }

    private static Integer countOrdersType31(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 3, 1);
    }

    private static Integer countOrdersType32(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 3, 2);
    }

    private static Integer countOrdersType33(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 3, 3);
    }

    private static Integer countOrdersType41(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 4, 1);
    }

    private static Integer countOrdersType42(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 4, 2);
    }

    private static Integer countOrdersType43(OrdersService ordersService) {
        return countOrdersByTypeAndStatus(ordersService, 4, 3);
    }

    private static Integer countOrdersHistoryType13(OrdersHistoryService ordersService) {
        return countOrdersHistoryByTypeAndStatus(ordersService, 1, 3);
    }

    private static Integer countOrdersHistoryType23(OrdersHistoryService ordersService) {
        return countOrdersHistoryByTypeAndStatus(ordersService, 2, 3);
    }

    private static Integer countOrdersHistoryType33(OrdersHistoryService ordersService) {
        return countOrdersHistoryByTypeAndStatus(ordersService, 3, 3);
    }

    private static Integer countOrdersHistoryType43(OrdersHistoryService ordersService) {
        return countOrdersHistoryByTypeAndStatus(ordersService, 4, 3);
    }

    public static OrderTotalVO countOrder(OrdersService ordersService) {
        OrderTotalVO vo = new OrderTotalVO();

        //学期统计
        vo.setSemesterOrderCount(CountOrdersUtils.countSemesterOrder(ordersService));
        vo.setLastMonthOrderCount(CountOrdersUtils.countLastMonthOrder(ordersService));
        vo.setCurrentMonthOrderCount(CountOrdersUtils.countThisMonthOrder(ordersService));
        vo.setCurrentWeekOrderCount(CountOrdersUtils.countOrdersThisWeek(ordersService));
        vo.setYesterdayOrderCount(CountOrdersUtils.countOrdersYesterday(ordersService));
        vo.setTodayOrderCount(CountOrdersUtils.countOrdersToday(ordersService));

        //订单数量显示
        vo.setOrderType11(CountOrdersUtils.countOrdersType11(ordersService));
        vo.setOrderType12(CountOrdersUtils.countOrdersType12(ordersService));
        vo.setOrderType21(CountOrdersUtils.countOrdersType21(ordersService));
        vo.setOrderType22(CountOrdersUtils.countOrdersType22(ordersService));
        vo.setOrderType31(CountOrdersUtils.countOrdersType31(ordersService));
        vo.setOrderType32(CountOrdersUtils.countOrdersType32(ordersService));
        vo.setOrderType41(CountOrdersUtils.countOrdersType41(ordersService));
        vo.setOrderType42(CountOrdersUtils.countOrdersType42(ordersService));

        return vo;
    }

    public static AOrdersCountVo countAOrders(OrdersService ordersService, OrdersHistoryService ordersHistoryService, RepairStationStatusService repairStationStatusService) {
        AOrdersCountVo vo = new AOrdersCountVo();
        vo.setLastMonthOrderCount(CountOrdersUtils.countLastMonthOrder(ordersService));
        vo.setCurrentMonthOrderCount(CountOrdersUtils.countThisMonthOrder(ordersService));
        vo.setCurrentWeekOrderCount(CountOrdersUtils.countOrdersThisWeek(ordersService));
        vo.setYesterdayOrderCount(CountOrdersUtils.countOrdersYesterday(ordersService));
        vo.setTodayOrderCount(CountOrdersUtils.countOrdersToday(ordersService));

        vo.setOrderType1(CountOrdersUtils.countOrdersType13(ordersService));
        vo.setOrderType2(CountOrdersUtils.countOrdersType23(ordersService));
        vo.setOrderType3(CountOrdersUtils.countOrdersType33(ordersService));
        vo.setOrderType4(CountOrdersUtils.countOrdersType43(ordersService));

        vo.setHistoryOrderType1(CountOrdersUtils.countOrdersHistoryType13(ordersHistoryService));
        vo.setHistoryOrderType2(CountOrdersUtils.countOrdersHistoryType23(ordersHistoryService));
        vo.setHistoryOrderType3(CountOrdersUtils.countOrdersHistoryType33(ordersHistoryService));
        vo.setHistoryOrderType4(CountOrdersUtils.countOrdersHistoryType43(ordersHistoryService));

        RepairStationStatus status = repairStationStatusService.getStatus();
        vo.setServerStatus(status.getServerStatus() == ServerStatus.OPPEN);
        vo.setOrderNotice(status.getOrderNotice());
        vo.setStopNotice(status.getStopNotice());
        vo.setStaffNotice(status.getStaffNotice());

        return vo;
    }
}
