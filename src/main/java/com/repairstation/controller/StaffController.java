package com.repairstation.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.repairstation.common.R;
import com.repairstation.domain.po.Orders;
import com.repairstation.domain.po.Staff;
import com.repairstation.domain.vo.StaffPasswordDto;
import com.repairstation.domain.vo.StaffSimpleVO;
import com.repairstation.service.OrdersService;
import com.repairstation.service.StaffService;
import com.repairstation.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
@Slf4j
public class StaffController {
    @Value("${project-config.csv-path}")
    private String csvPath;
    @Autowired
    private StaffService staffService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private CSVUtils csvUtils;

    @PostMapping("/login")
    public R<String> login(@RequestBody Staff staff) {
        log.info("staff login: {}", staff.toString());
        //验证长度
        if (staff.getPassword().length() != 32) return R.error("账号或密码错误请重新登陆");

        //将加密密码再次加密
        String password = DigestUtils.md5DigestAsHex(staff.getPassword().getBytes());

        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Staff::getPassword, password);
        queryWrapper.eq(Staff::getStudentId, staff.getStudentId());

        if (staffService.count(queryWrapper) != 1) return R.error("账号或密码错误请重新登陆");

        Staff staffOne = staffService.getOne(queryWrapper);

        //生成JWT令牌
        Map<String, Object> map = new HashMap<>();
        //id一定要变字符串，Long类型转换会有问题的
        map.put("id", String.valueOf(staffOne.getId()));
        map.put("studentId", staffOne.getStudentId());
        map.put("name", staffOne.getName());

        String jwt = JWTUtils.generateJwt(map, password);
        if (!RedisUtils.saveStaff(redisTemplate, jwt, map)) {
            log.info("拒绝登录");
            return R.error("检测到账号已在别的设备登录<br />请退出登录后重新尝试<br />╮(๑•́ ₃•̀๑)╭");
        }


        return R.success(jwt);
    }

    @Cacheable(value = "leaderboardCache", key = "0")
    @GetMapping("/leaderboard")
    public R<List<StaffSimpleVO>> getOrdersLeaderboard() {
        return R.success(staffService.getStaffOrderCounts());
    }

    @PutMapping("/change-password")
    public R<String> changePassword(HttpServletRequest request, @RequestBody StaffPasswordDto staffPasswordDto) {
        String id = JWTUtils.getIdByRequest(request);
        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Staff::getId, id);
        queryWrapper.eq(Staff::getPassword,  DigestUtils.md5DigestAsHex(staffPasswordDto.getOldPassword().getBytes()));
        if (staffService.count(queryWrapper) != 1) return R.error("密码错误请重新输入");

        Staff staff = staffService.getOne(queryWrapper);
        staff.setPassword( DigestUtils.md5DigestAsHex(staffPasswordDto.getNewPassword().getBytes()));
        staffService.updateById(staff);
        return R.success("修改成功");
    }

    @GetMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        RedisUtils.exitStaff(redisTemplate, request.getHeader("Authorization"));
        return R.success("成功退出");
    }

    @GetMapping("/order-counts")
    public R<List<StaffSimpleVO>> getStaffOrderCounts(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        return R.success(staffService.getStaffOrderCountsInTimeRange(startTime, endTime));
    }

    @GetMapping("/staff-list")
    public R<Page<Staff>> getStaffList(int page, int pageSize, String name) {
        log.info("执行分页查询...");
        Page<Staff> staffPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        if (StringUtils.isNoneEmpty(name)) {
            queryWrapper.like(Staff::getName, name)
                    .or().like(Staff::getStudentId, name);
        }

        queryWrapper.ne(Staff::getName, "admin");

        queryWrapper.orderByDesc(Staff::getMajor);

        queryWrapper.orderByDesc(Staff::getStudentId);
        staffService.page(staffPage, queryWrapper);
        return R.success(staffPage);
    }

    @PostMapping("/addStaff")
    public R<String> addStaff(@RequestBody Staff staff) {
        String password = DigestUtils.md5DigestAsHex("A888888".getBytes());
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        staff.setPassword(password);
        staffService.save(staff);
        return R.success("添加成功");
    }

    @PostMapping("/addStaff-list")
    public R<String> addStaffList(@RequestBody List<Staff> staffList) {
        for (Staff staff : staffList) {
            String password = DigestUtils.md5DigestAsHex("A888888".getBytes());
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            staff.setPassword(password);
        }
        staffService.saveBatch(staffList);
        return R.success("批量添加成功");
    }

    @PutMapping("/reset")
    public R<String> resetPassword(@RequestBody Staff staff) {
        String password = DigestUtils.md5DigestAsHex("A888888".getBytes());
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        staff.setPassword(password);
        staffService.updateById(staff);
        return R.success("重置成功");
    }

    @GetMapping("/info")
    public R<Staff> getStaffInfo(@RequestParam String id) {
        Staff staff = staffService.getById(id);
        staff.setPassword(null);
        return R.success(staff);
    }

    @PutMapping("/complete-info")
    public R<String> completeInfo(@RequestBody Staff staff) {
        //预防恶性注入
        Staff s = staffService.getById(staff.getId());
        s.setId(staff.getId());
        s.setMajor(staff.getMajor());
        s.setPhone(staff.getPhone());
        s.setPoliticalStatus(staff.getPoliticalStatus());

        staffService.updateById(s);
        return R.success("信息补充成功！");
    }

    @DeleteMapping("/delete/{id}")
    public R<String> remove(@PathVariable String id) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getStaffId, id);
        queryWrapper.eq(Orders::getStatus, 2);
        if (ordersService.count(queryWrapper) > 0) return R.error("用户订单没完成，不能删除！");

        staffService.removeById(id);
        return R.success("删除成功");
    }

    @GetMapping("/getNameById")
    public R<String> getNameById(@RequestParam String id) {
        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Staff::getId, id);
        Staff staff = staffService.getOne(queryWrapper);
        return R.success(staff.getName());
    }

    @GetMapping("/staff-online")
    public R<Integer> getStaffOnline() {
        return R.success(RedisUtils.countOnlineStaff(redisTemplate));
    }

    @GetMapping("/online-name")
    public R<List<String>> getStaffName() {
        return R.success(RedisUtils.onlineStaffName(redisTemplate));
    }

    /**
     * 下载成员信息
     * @param response
     */
    @GetMapping("/csv")
    public void getStaffCsv(HttpServletResponse response) {
        String s = csvUtils.creatStaffCSV();
        s = csvPath + File.separator + s;
        ScheduledUtils.delFile10Min(s);
        DownloadUtil.download(response, s, FileUtil.getName(s));
    }

    /**
     * 下载修单日期记录
     * @param response
     * @param startTime
     * @param endTime
     */
    @GetMapping("/count-csv")
    public void getCountCsv(HttpServletResponse response,
                            @RequestParam(required = false) String startTime,
                            @RequestParam(required = false) String endTime) {

        List<StaffSimpleVO> staffOrderCounts = staffService.getStaffOrderCountsInTimeRange(startTime, endTime);
       
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isBlank(startTime)) {
            startTime = LocalDateTime.now().format(formatter);
        }

        String s = csvUtils.creatOrderCount(staffOrderCounts, LocalDateTime.parse(startTime, formatter));
        s = csvPath + File.separator + s;
        ScheduledUtils.delFile10Min(s);
        DownloadUtil.download(response, s, FileUtil.getName(s));
    }
}
