package com.power.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.annotation.MyLimit;
import com.power.domain.ShiXiLog;
import com.power.domain.vo.Result;
import com.power.service.ShiXiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/logs")
@CrossOrigin
@MyLimit(prefix = "logs")
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
    @MyLimit(prefix = "getByUserId", limit = 20, windowSize = 60)
    public Result getByUserId(@PathVariable("userId") Long userId) {
        LambdaQueryWrapper<ShiXiLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShiXiLog::getUserId, userId);
        queryWrapper.orderByDesc(ShiXiLog::getCreateTime);

        List<ShiXiLog> list = shiXiLogService.list(queryWrapper);
        for (ShiXiLog shiXiLog : list) {
            String username = shiXiLog.getUsername();
            shiXiLog.setUsername(username.substring(username.length() - 4));

            String realName = shiXiLog.getRealName();
            if (StrUtil.isNotBlank(realName) && !"-".equals(realName)) {
                StringBuilder desensitizationRealNam = new StringBuilder(realName);
                desensitizationRealNam.setCharAt(realName.length() - 2, '*');
                shiXiLog.setRealName(desensitizationRealNam.toString());
            }
        }

        return Result.success(list);
    }

}
