package com.rs.controller.repair;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rs.common.R;
import com.rs.domain.po.Orders;
import com.rs.domain.po.Staff;
import com.rs.domain.vo.StaffPasswordDto;
import com.rs.domain.vo.StaffSimpleVO;
import com.rs.service.OrdersService;
import com.rs.service.StaffService;
import com.rs.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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

        Staff staffOne = staffService.getOne(queryWrapper);

        if (staffOne == null) return R.error("账号或密码错误请重新登陆");

        //生成JWT令牌
        String jwt = JWTUtils.generateJwt(staffOne, password);


        Map<String, Object> claims = new HashMap<>();
        //id一定要变字符串，Long类型转换会有问题的
        claims.put("id", String.valueOf(staffOne.getId()));
        claims.put("studentId", staffOne.getStudentId());
        claims.put("name", staffOne.getName());

        if (!RSRedisUtils.saveStaff(redisTemplate, jwt, claims)) {
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
        RSRedisUtils.exitStaff(redisTemplate, request.getHeader("Authorization"));
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
        Page<Staff> staffPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Staff> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        if (StrUtil.isNotEmpty(name)) {
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
        return R.success(RSRedisUtils.countOnlineStaff(redisTemplate));
    }

    @GetMapping("/online-name")
    public R<List<String>> getStaffName() {
        return R.success(RSRedisUtils.onlineStaffName(redisTemplate));
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

        if (StrUtil.isBlank(startTime)) {
            startTime = LocalDateTime.now().format(formatter);
        }

        String s = csvUtils.creatOrderCount(staffOrderCounts, LocalDateTime.parse(startTime, formatter));
        s = csvPath + File.separator + s;
        ScheduledUtils.delFile10Min(s);
        DownloadUtil.download(response, s, FileUtil.getName(s));
    }
}
