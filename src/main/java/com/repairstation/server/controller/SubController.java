package com.repairstation.server.controller;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.repairstation.common.R;
import com.repairstation.domain.po.Sub;

import com.repairstation.domain.po.SubStatus;
import com.repairstation.domain.vo.VerificationCode;
import com.repairstation.server.service.SubService;
import com.repairstation.server.service.SubStatusService;
import com.repairstation.utils.RandomDateUtils;
import com.repairstation.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/sub")
@Slf4j
public class SubController {
    @Autowired
    private SubService subService;
    @Autowired
    private SubStatusService subStatusService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostMapping
    public R<String> addSub(@RequestBody Sub sub) {
        subService.save(sub);
        return R.success("添加成功");
    }

    @PutMapping
    public R<String> updateSub(@RequestBody Sub sub) {
        subService.updateById(sub);
        return R.success("修改成功");
    }

    @GetMapping("/stu")
    public R<List<Sub>> getRandomSub() {
        Sub sub = new Sub();
        sub.setTopic("我们维修站是为大家免费提供电脑维修的地方，那么以下哪个是维修站的报修方式（）");
        sub.setOption1("在“ITeam维修站”公众号");
        sub.setOption2("在“农工商计算机学院ITeam基地”公众号");
        sub.setOption3("在易班首页");
        sub.setOption4("以上都是");
        sub.setResult(4);

        SubStatus subStatus = subStatusService.getSubStatus();
        List<Sub> randomSubs = subService.getRandomSubs(String.valueOf(subStatus.getNumber() - 1));
        randomSubs.add(sub);

        Collections.shuffle(randomSubs);
        return R.success(randomSubs);
    }

    @DeleteMapping("/{id}")
    public R<String> deleteSub(@PathVariable String id) {
        subService.removeById(id);
        return R.success("删除成功！");
    }

    @PostMapping("/list")
    public R<String> addSubList(@RequestBody List<Sub> subList) {
        subService.saveBatch(subList);
        return R.success("添加成功");
    }

    @GetMapping("/list")
    public R<Page<Sub>> getSubList(int page, int pageSize, String name) {
        Page<Sub> subPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Sub> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneEmpty(name)) {
            queryWrapper.like(Sub::getTopic, name);
        }

        queryWrapper.orderByDesc(Sub::getCreateTime);
        subService.page(subPage, queryWrapper);

        return R.success(subPage);
    }

    @GetMapping("/v-code")
    public R<VerificationCode> getVerificationCode() {
        // 尝试从 Redis 获取验证码
        VerificationCode existingCode = RedisUtils.getCodeFromRedis(redisTemplate);

        if (existingCode != null) {
            existingCode.setCode(existingCode.getCode() + ',' + PinyinUtil.getPinyin(existingCode.getCode(), " "));

            // 如果已有验证码，直接返回
            return R.success(existingCode);
        }

        String code = RandomDateUtils.RandomChineseDateExample();

        VerificationCode v = RedisUtils.setCodeWithExpiration(redisTemplate, code);
        code = code + ',' + PinyinUtil.getPinyin(code, " ");
        v.setCode(code);

        return R.success(v);
    }

}
