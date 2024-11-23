package com.repairstation.server.controller;

import com.repairstation.common.R;
import com.repairstation.domain.po.SubStatus;
import com.repairstation.server.service.SubStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sub-status")
@Slf4j
public class SubStatusController {
    @Autowired
    private SubStatusService subStatusService;

    @GetMapping
    public R<SubStatus> getSubStatus(){
        return R.success(subStatusService.getSubStatus());
    }

    @PutMapping
    public R<SubStatus> updateSubStatus(@RequestBody SubStatus subStatus){
        subStatusService.updateById(subStatus);
        return R.success(subStatus);
    }
}
