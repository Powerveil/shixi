package com.power.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.domain.ShiXiLog;
import com.power.domain.User;
import com.power.domain.vo.Result;
import com.power.domain.vo.UserListVo;
import com.power.service.ShiXiLogService;
import com.power.service.UserService;
import com.power.utils.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserManager {
    @Autowired
    private UserService userService;

    @Autowired
    private ShiXiLogService shiXiLogService;

    public Result addUser(User user) {
        user.setAddress(AddressUtil.getAddrByLocateStr(user.getLocation()));
        if (userService.save(user)) {
            return Result.success(null);
        } else {
            return Result.error("501", "保存失败");
        }
    }

    public Result modifyUser(User user) {
        user.setAddress(AddressUtil.getAddrByLocateStr(user.getLocation()));
        if (userService.updateById(user)) {
            return Result.success(null);
        } else {
            return Result.error("501", "修改失败");
        }
    }

    public Result userListLogs() {
        List<UserListVo> result = new ArrayList<>(100);

        List<User> list = userService.list();
        for (User user : list) {
            UserListVo userListVo = new UserListVo();

            LambdaQueryWrapper<ShiXiLog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShiXiLog::getUserId, user.getId());
            queryWrapper.orderByDesc(ShiXiLog::getCreateTime);
            List<ShiXiLog> shiXiLogList = shiXiLogService.list(queryWrapper);

            BeanUtil.copyProperties(user, userListVo);
            boolean todaySuccess = shiXiLogList.isEmpty() ? false : (!LocalDateTimeUtil.isSameDay(LocalDateTime.now(), shiXiLogList.get(0).getCreateTime()) ? false : shiXiLogList.get(0).getSuccess());
            userListVo.setTodaySuccess(todaySuccess);
            userListVo.setShiXiLogList(shiXiLogList);

            result.add(userListVo);
        }

        return Result.success(result);
    }

    public Result userList(List<Long> idListStr) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (!CollUtil.isEmpty(idListStr)) {
            queryWrapper.in(User::getId, idListStr);
            queryWrapper.orderByAsc(User::getId);
        }

        return Result.success(userService.list(queryWrapper));
    }

    public Result removeUser(Long userId) {
        if (userService.removeById(userId)) {
            return Result.success(null);
        } else {
            return Result.error("501", "删除失败");
        }
    }
}
