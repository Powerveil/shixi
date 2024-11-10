package com.power.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
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
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserManager {
    @Autowired
    private UserService userService;

    @Autowired
    private ShiXiLogService shiXiLogService;

    SymmetricCrypto aes = new SymmetricCrypto("AES");

    Map<String, String> superAdmin;

    @PostConstruct
    public void init() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId, CollUtil.newArrayList(1, 3));
        List<User> list = userService.list(queryWrapper);
        superAdmin = list.stream().collect(Collectors.toMap(User::getUsername, User::getPassword));
    }

    public Result addUser(User user) {

        user.setAddress(AddressUtil.getAddrByLocateStr(user.getLocation()));

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User one = userService.getOne(queryWrapper);
        if (one != null) {
            return Result.error("501", "用户已经存在");
        }

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
            user.setPassword(null);
        }
        for (User user : list) {
            UserListVo userListVo = new UserListVo();

            LambdaQueryWrapper<ShiXiLog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShiXiLog::getUserId, user.getId());
            queryWrapper.orderByDesc(ShiXiLog::getCreateTime);
            queryWrapper.last("limit 1");
            ShiXiLog shiXiLog = shiXiLogService.getOne(queryWrapper);

            BeanUtil.copyProperties(user, userListVo);
            boolean todaySuccess = !Objects.isNull(shiXiLog) && LocalDateTimeUtil.isSameDay(LocalDateTime.now(), shiXiLog.getCreateTime()) && shiXiLog.getSuccess();
            userListVo.setTodaySuccess(todaySuccess);
//            userListVo.setShiXiLogList(shiXiLogList);
            userListVo.setPassword(null);
            String username = user.getUsername();
            userListVo.setUsername(username.substring(username.length() - 4));
            String realName = userListVo.getRealName();
            if (StrUtil.isNotBlank(realName) && !"-".equals(realName)) {
                StringBuilder desensitizationRealNam = new StringBuilder(realName);
                desensitizationRealNam.setCharAt(realName.length() - 2, '*');
                userListVo.setRealName(desensitizationRealNam.toString());
            }

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

    public Result removeUser(Long userId, String password) {

        User user = userService.getById(userId);

        boolean flag = false;
        for (Map.Entry <String, String> entry : superAdmin.entrySet()) {
            if (entry.getValue().equals(password)) {
                flag = true;
            }
        }

        if (!flag && !user.getPassword().equals(password)) {
            return Result.error("501", "只有本人和管理员可以删除账号！");
        }

        if (userService.removeById(userId)) {
            return Result.success(null);
        } else {
            return Result.error("501", "删除失败");
        }
    }

    public Result getById(Long userId, String password) {
//        User user = userService.getById(userId);
//        String username = aes.decryptStr(token);
//        // 超级管理员或者密码匹配的可以获取
//        if (superAdmin.containsKey(username) || (Objects.nonNull(user) && user.getPassword().equals(username))) {
//            return Result.success(user);
//        } else {
//            return Result.error("501", "只能获取自己的信息");
//        }

        User user = userService.getById(userId);

        boolean flag = false;
        for (Map.Entry <String, String> entry : superAdmin.entrySet()) {
            if (entry.getValue().equals(password)) {
                flag = true;
            }
        }

        // 超级管理员或者密码匹配的可以获取
        if (flag || (Objects.nonNull(user) && user.getPassword().equals(password))) {
            return Result.success(user);
        } else {
            return Result.error("501", "只能获取自己的信息");
        }
    }

//    public Result getToken(Long userId, String password) {
//        // 超级管理员直接返回自己的token
//        for (Map.Entry <String, String> entry : superAdmin.entrySet()) {
//            if (entry.getValue().equals(password)) {
//                return Result.success(aes.encryptHex(entry.getKey()));
//            }
//        }
//
//        User user = userService.getById(userId);
//        if (Objects.nonNull(user) && user.getPassword().equals(password)) {
//            return Result.success(aes.encryptHex(user.getUsername()));
//        } else {
//            return Result.error("501", "密码错误");
//        }
//    }

    public Result getAdminToken() {
        List<Object> list = CollUtil.newArrayList();
        superAdmin.forEach((k, v) -> {
            list.add(aes.encryptHex(k));
        });
        return Result.success(list);
    }
}
