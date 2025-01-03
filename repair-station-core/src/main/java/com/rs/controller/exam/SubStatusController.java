package com.rs.controller.exam;


import com.rs.common.R;
import com.rs.domain.po.SubStatus;
import com.rs.service.SubStatusService;
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
