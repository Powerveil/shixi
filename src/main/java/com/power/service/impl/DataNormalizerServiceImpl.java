package com.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.power.domain.ShiXiLog;
import com.power.domain.User;
import com.power.service.DataNormalizerService;
import com.power.service.ShiXiLogService;
import com.power.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DataNormalizerServiceImpl implements DataNormalizerService {

    @Autowired
    private UserService userService;

    @Autowired
    private ShiXiLogService shiXiLogService;


    @Override
    public void dataNormalizer() {
        List<User> list = userService.list();
        list.stream()
                .filter(item -> Objects.nonNull(item.getRealName()))
                .forEach(user -> {
                    LambdaUpdateWrapper<ShiXiLog> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper
                            .set(ShiXiLog::getRealName, user.getRealName())
                            .eq(ShiXiLog::getUserId, user.getId())
                            .eq(ShiXiLog::getRealName, "-");
                    shiXiLogService.update(updateWrapper);
                });
    }
}
