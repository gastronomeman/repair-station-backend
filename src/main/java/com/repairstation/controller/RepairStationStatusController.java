package com.repairstation.controller;

import com.repairstation.common.R;
import com.repairstation.domain.po.RepairStationStatus;
import com.repairstation.service.RepairStationStatusService;
import com.repairstation.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wxzStatus")
@Slf4j
public class RepairStationStatusController {
    @Autowired
    RepairStationStatusService repairStationStatusService;

    @Cacheable(value = "wxzCache", key = "'status'")
    @GetMapping
    public R<RepairStationStatus> getStatus() {
        RepairStationStatus status = repairStationStatusService.getStatus();
        return R.success(status);
    }

    @Cacheable(value = "wxzCache", key = "'orderTitle'")
    @GetMapping("/orderTitle")
    public R<String> getOrderTitle() {
        RepairStationStatus status = repairStationStatusService.getStatus();
        return R.success(status.getOrderNotice());
    }

    @Cacheable(value = "wxzCache", key = "'staffTitle'")
    @GetMapping("/staffTitle")
    public R<String> getStaffTitle() {
        RepairStationStatus status = repairStationStatusService.getStatus();
        return R.success(status.getStaffNotice());
    }

    @Cacheable(value = "wxzCache", key = "'stopTitle'")
    @GetMapping("/stopTitle")
    public R<String> getStopTitle() {
        RepairStationStatus status = repairStationStatusService.getStatus();
        return R.success(status.getStopNotice());
    }

    @Cacheable(value = "wxzCache", key = "'tool'")
    @GetMapping("/tool-box")
    public R<String> getToolBox() {
        RepairStationStatus status = repairStationStatusService.getStatus();
        return R.success(status.getToolBox());
    }

    @CacheEvict(value = "wxzCache", key = "'tool'")
    @PutMapping("/tool-box")
    public R<String> setToolBox(@RequestBody RepairStationStatus status , HttpServletRequest req) {
        String id = JWTUtils.getIdByRequest(req);
        log.info("有同学修改了工具位置：{}", id);

        RepairStationStatus s = repairStationStatusService.getStatus();
        s.setToolBox(status.getToolBox());
        repairStationStatusService.updateById(s);
        return R.success("修改成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "admin", key = "'total'"),// admin面板的申请也有title
            @CacheEvict(value = "wxzCache", key = "'status'")
    })
    @GetMapping("/chang-status")
    public R<String> changeStatus() {
        RepairStationStatus rs = repairStationStatusService.getStatus();

        if (rs.getServerStatus() == 0) rs.setServerStatus(1);
        else rs.setServerStatus(0);

        repairStationStatusService.updateById(rs);
        return R.success("服务器切换状态成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "admin", key = "'total'"),// admin面板的申请也有title
            @CacheEvict(value = "wxzCache", key = "'orderTitle'")
    })
    @PutMapping("/chang-order-notice")
    public R<String> changeOrderNotice(@RequestBody RepairStationStatus s) {
        RepairStationStatus rs = repairStationStatusService.getStatus();
        rs.setOrderNotice(s.getOrderNotice());
        repairStationStatusService.updateById(rs);
        return R.success("公告更改成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "admin", key = "'total'"),// admin面板的申请也有title
            @CacheEvict(value = "wxzCache", key = "'staffTitle'")
    })
    @PutMapping("/chang-staff-notice")
    public R<String> changeStaffNotice(@RequestBody RepairStationStatus s) {
        RepairStationStatus rs = repairStationStatusService.getStatus();
        rs.setStaffNotice(s.getStaffNotice());
        repairStationStatusService.updateById(rs);
        return R.success("公告更改成功");
    }

    @Caching(evict = {
            @CacheEvict(value = "admin", key = "'total'"),// admin面板的申请也有title
            @CacheEvict(value = "wxzCache", key = "'stopTitle'")
    })
    @PutMapping("/chang-stop-notice")
    public R<String> changeStopNotice(@RequestBody RepairStationStatus s) {
        RepairStationStatus rs = repairStationStatusService.getStatus();
        rs.setStopNotice(s.getStopNotice());
        repairStationStatusService.updateById(rs);
        return R.success("公告更改成功");
    }
}
