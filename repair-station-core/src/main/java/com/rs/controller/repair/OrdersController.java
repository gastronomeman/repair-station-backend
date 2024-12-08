package com.rs.controller.repair;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rs.common.R;
import com.rs.domain.dto.OrdersWithStaffNameDto;
import com.rs.domain.po.Orders;
import com.rs.domain.po.OrdersHistory;
import com.rs.domain.vo.AOrdersCountVo;
import com.rs.domain.vo.OrdersSimpleVO;
import com.rs.domain.vo.OrderTotalVO;
import com.rs.domain.vo.StaffOrderTotalVO;
import com.rs.enums.OrderStatus;
import com.rs.service.OrdersHistoryService;
import com.rs.service.OrdersService;
import com.rs.utils.JWTUtils;
import com.rs.utils.RSRedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FilenameFilter;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/orders")
@Slf4j
public class OrdersController {
    @Value("${project-config.photo-path}")
    private String photoPath;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersHistoryService ordersHistoryService;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @GetMapping("/total")
    @Cacheable(value = "order", key = "'total'")
    public R<OrderTotalVO> getStaffOrderTotal() {
        return R.success(ordersService.getOrderTotal());
    }

    @Cacheable(value = "admin", key = "'total'")
    @GetMapping("/admin-orders")
    public R<AOrdersCountVo> getAdminOrders() {
        return R.success(ordersService.getAOrdersCount());
    }

    @GetMapping("/type-list")
    @Cacheable(value = "orderList1", key = "'[' + #type + ']'")
    public R<List<Orders>> getOrdersTypeList(@RequestParam Integer type, @RequestParam Integer status) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderType, type);
        queryWrapper.eq(Orders::getStatus, status);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        return R.success(ordersService.list(queryWrapper));
    }

    @Cacheable(value = "orderList2", key = "'[' + #type + ']'")
    @GetMapping("/type-list-with-name")
    public R<List<OrdersWithStaffNameDto>> getOrdersTypeListWithName(@RequestParam Integer type, @RequestParam Integer status) {
        return R.success(ordersService.getOrdersWithStaffNameList(type, status));
    }


    @Caching(evict = {
            @CacheEvict(value = "order", key = "'total'"),
            @CacheEvict(value = "admin", key = "'total'"),
            @CacheEvict(value = "orderList1", key = "'[' + #orders.orderType.value + ']'")
    })
    @PostMapping
    public R<String> addOrders(@RequestBody Orders orders) {
        ordersService.addOrder(orders);
        return R.success("添加成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "order", key = "'total'"),
            @CacheEvict(value = "admin", key = "'total'"),
            @CacheEvict(value = "orderList1", key = "'[' + #orders.orderType.value + ']'"),
            @CacheEvict(value = "orderList2", key = "'[' + #orders.orderType.value + ']'")
    })
    @PutMapping("/taking")
    public R<String> taking(HttpServletRequest req, @RequestBody Orders orders)  {
        Long id = Long.valueOf(JWTUtils.getIdByRequest(req));
        if (orders.getStatus() != OrderStatus.WAIT) return R.error("抢单失败");

        ordersService.takingOrder(id, orders);

        return R.success("抢单成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "leaderboardCache", key = "0"),
            @CacheEvict(value = "order", key = "'total'"),
            @CacheEvict(value = "admin", key = "'total'"),
            @CacheEvict(value = "orderList2", key = "'[' + #orders.orderType.value + ']'")
    })
    @PutMapping("/finish-order")
    public R<String> finishOrder(@RequestBody Orders orders)  {
        orders.setStatus(OrderStatus.COMPLETE);
        orders.setCompletionTime(LocalDateTime.now());

        ordersService.updateById(orders);
        return R.success("恭喜你完成维修，辛苦啦！");
    }

    @Caching(evict = {
            @CacheEvict(value = "leaderboardCache", key = "0"),
            @CacheEvict(value = "order", key = "'total'"),
            @CacheEvict(value = "admin", key = "'total'"),
    })
    @PutMapping("/chang-status1/{id}")
    public R<String> changStatus1(@PathVariable String id) {
        Orders orders = ordersService.getById(id);
        if (orders.getStatus() == OrderStatus.COMPLETE) return R.error("订单已完成，不可恢复待接单");
        else if (orders.getStatus() == OrderStatus.WAIT) return R.success("修改成功");

        orders.setStatus(OrderStatus.WAIT);
        orders.setAssignor(orders.getStaffId());
        ordersService.updateById(orders);

        RSRedisUtils.removeOrderList(redisTemplate);
        return R.success("修改成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "leaderboardCache", key = "0"),
            @CacheEvict(value = "order", key = "'total'"),
            @CacheEvict(value = "admin", key = "'total'"),
    })
    @PutMapping("/chang-status2/{id}")
    public R<String> changStatus2(@PathVariable String id) {
        Orders orders = ordersService.getById(id);
        if (orders.getStatus() == OrderStatus.WAIT) return R.error("没人接单，不可更改为维修中");
        else if (orders.getStatus() == OrderStatus.REPAIR) return R.success("修改成功");

        orders.setStatus(OrderStatus.REPAIR);
        ordersService.updateById(orders);

        RSRedisUtils.removeOrderList(redisTemplate);
        return R.success("修改成功");
    }

    @CacheEvict(value = "order", key = "'total'")
    @PutMapping("/transfer-order/{id}")
    public R<String> transferOrder(@PathVariable String id) {
        Orders orders = ordersService.getById(id);

        if (orders.getAssignor() != null) return R.error("订单已转让过，不可再次转让！");

        orders.setStatus(OrderStatus.WAIT);
        orders.setAssignor(orders.getStaffId());
        orders.setStaffId(null);

        ordersService.updateById(orders);

        RSRedisUtils.removeOrderList(redisTemplate);
        return R.success("订单转让成功");
    }

    @GetMapping("/staff-total")
    public R<StaffOrderTotalVO> staffOrderTotal(HttpServletRequest request) {
        String staffId = JWTUtils.getIdByRequest(request);

        StaffOrderTotalVO s = new StaffOrderTotalVO();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Orders::getStaffId, staffId);
        queryWrapper.eq(Orders::getStatus, 3);
        s.setOrderTotal((int) ordersService.count(queryWrapper));

        LocalDateTime start = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
        queryWrapper.between(Orders::getCompletionTime, start, end);
        s.setWeekOrderTotal((int) ordersService.count(queryWrapper));

        return R.success(s);
    }

    @GetMapping("/order-list")
    public R<Page<Orders>> getFinishOrder(HttpServletRequest req, int page, int pageSize, String name) {
        return getOrdersPage(req, page, pageSize, name,
                ordersService,
                Orders::getDormitory,
                Orders::getName,
                Orders::getOrderDescribe,
                Orders::getStaffId,
                Orders::getCreateTime);
    }

    @GetMapping("/history-list")
    public R<Page<OrdersHistory>> getOrdersHistoryList(HttpServletRequest req, int page, int pageSize, String name) {
        return getOrdersPage(req, page, pageSize, name,
                ordersHistoryService,
                OrdersHistory::getDormitory,
                OrdersHistory::getName,
                OrdersHistory::getOrderDescribe,
                OrdersHistory::getStaffId,
                OrdersHistory::getCreateTime);
    }

    @GetMapping("/taking-list")
    public R<List<Orders>> getOrdersTypeList(HttpServletRequest req) {
        String id = JWTUtils.getIdByRequest(req);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStaffId, id);
        queryWrapper.eq(Orders::getStatus, 2);
        queryWrapper.orderByDesc(Orders::getCreateTime);

        return R.success(ordersService.list(queryWrapper));
    }

    @GetMapping("/uploader-list")
    public R<List<Orders>> getUploaderList(HttpServletRequest req) {
        List<Orders> list = getOrdersTypeList(req).getData();

        list = list.stream()
                .filter(orders -> !hasFileWithNameContaining(String.valueOf(orders.getId())))
                .toList();

        return R.success(list);
    }


    @GetMapping("/staff-list")
    public R<List<Orders>> getStaffList(@RequestParam String id,
                                        @RequestParam(required = false) String startTime,
                                        @RequestParam(required = false) String endTime) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStaffId, id);
        queryWrapper.eq(Orders::getStatus, 3);

        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            queryWrapper.between(Orders::getCompletionTime, startTime, endTime);
        }

        queryWrapper.orderByDesc(Orders::getCreateTime);

        return R.success(ordersService.list(queryWrapper));
    }

    @GetMapping("/change-sql")
    public R<String> changeSql() {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStatus, 1).or().eq(Orders::getStatus, 2);
        if (ordersService.count(queryWrapper) > 0) return R.error("有订单未完成，请完成所有订单后再进行转换！");

        ordersService.changeSql();
        RSRedisUtils.removeAllTotal(redisTemplate);

        return R.success("转换成功，正在生成下载链接...");
    }

    @GetMapping
    public R<List<OrdersSimpleVO>> getOrders(@RequestParam String name) {

        if (StrUtil.isBlank(name)) return R.error("空白不能查");

        List<OrdersSimpleVO> list = ordersService.getOrderSimpleList(name);

        return R.success(list);
    }

    @PutMapping("/cancel")
    public R<String> cancelOrder(@RequestBody Orders order) {
        ordersService.cancel(order);
        RSRedisUtils.removeAllTotal(redisTemplate);
        return R.success("完成作废");
    }


    private <T> R<Page<T>> getOrdersPage(HttpServletRequest req, int page, int pageSize, String name,
                                         IService<T> service,
                                         SFunction<T, ?> dormField,
                                         SFunction<T, ?> nameField,
                                         SFunction<T, ?> descField,
                                         SFunction<T, ?> staffField,
                                         SFunction<T, ?> timeField) {
        String id = JWTUtils.getIdByRequest(req);

        // 构建分页对象
        Page<T> pageObj = new Page<>(page, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(name)) {
            queryWrapper.like(dormField, name)
                    .or().like(nameField, name)
                    .or().like(descField, name);
        }

        // 非管理员用户根据 StaffId 筛选
        if (!"20240704".equals(id)) {
            queryWrapper.eq(staffField, id);
        }

        // 按时间倒序
        queryWrapper.orderByDesc(timeField);

        // 分页查询
        service.page(pageObj, queryWrapper);

        return R.success(pageObj);
    }

    private boolean hasFileWithNameContaining(String substring) {
        File folder = new File(photoPath);

        // 检查文件夹是否存在并且是一个目录
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("指定的路径不是一个有效的目录");
            return false;
        }

        // 使用 FilenameFilter 过滤器来查找包含指定子串的文件
        FilenameFilter filter = (dir, name) -> name.contains(substring);

        // 获取所有符合条件的文件
        File[] matchingFiles = folder.listFiles(filter);

        // 检查是否有匹配的文件
        return matchingFiles != null && matchingFiles.length > 0;
    }

}
