package com.power.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.domain.ShiXiLog;
import com.power.domain.vo.Result;
import com.power.service.ShiXiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@CrossOrigin
public class ShiXiLogController {

    @Autowired
    private ShiXiLogService shiXiLogService;


//    @GetMapping("/list")
//    //todo 加状态
//    public Result shiXiLogList() {
//        new LambdaQueryChainWrapper<>()
//        return Result.success(shiXiLogService.list());
//    }

    @PostMapping("/add")
    public Result addShiXiLog(@RequestBody List<ShiXiLog> shiXiLogList) {
        if (shiXiLogService.saveBatch(shiXiLogList)) {
            return Result.success(null);
        } else {
            return Result.error("501", "保存失败");
        }
    }

    @GetMapping("/getByUserId/{userId}")
    public Result getByUserId(@PathVariable("userId") Long userId) {
        LambdaQueryWrapper<ShiXiLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShiXiLog::getUserId, userId);
        queryWrapper.orderByDesc(ShiXiLog::getCreateTime);
        return Result.success(shiXiLogService.list(queryWrapper));
    }

}
