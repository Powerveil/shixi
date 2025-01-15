package com.power.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONUtil;
import com.power.annotation.IPLimit;
import com.power.domain.User;
import com.power.domain.vo.Result;
import com.power.domain.vo.UserListVo;
import com.power.manager.UserManager;
import com.power.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/command")
@CrossOrigin
@Slf4j
@IPLimit(prefix = "command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @Autowired
    private UserManager userManager;


    @GetMapping("/test")
    public String test(@RequestParam("commandStr") String commandStr) {
        log.info("运行的命令:{}", commandStr);
        String result = RuntimeUtil.execForStr(commandStr);
        log.info("运行结果:\n{}", result);
        return result;
    }

    @PostMapping("/exec")
    @IPLimit(prefix = "exec")
    public Result exec(@RequestBody(required = false) List<Long> idList, HttpServletRequest request) {
        if (CollUtil.isEmpty(idList)) {
            return Result.error("501", "请选择批量打卡的用户~");
        }
        log.info("批量打卡已启动");

        StringBuilder result = new StringBuilder();
        for (Long l : idList) {
            result.append(" ").append(l);
        }
        Result exec = commandService.exec("node /root/deploy/shixi/front/xybSign-node/index.js" + result);
        log.info("脚本运行结果：{}", JSONUtil.toJsonPrettyStr(exec));
        log.info("批量打卡已完成");

        return exec;
    }
    ///command/exec



    @GetMapping("/exec2/{idStr}")
    @IPLimit(prefix = "batch", limit = 20, windowSize = 60)
    public Result exec2(@PathVariable("idStr") String idStr, HttpServletRequest request) {
        if (Objects.isNull(idStr)) {
            return Result.error("501", "请选择打卡的用户~");
        }
        log.info("打卡已启动");

        StringBuilder result = new StringBuilder();

        String[] split = idStr.split(",");

        Set<String> idList = new LinkedHashSet<>(Arrays.asList(split));

        for (String id : idList) {
            result.append(" ").append(id);
        }

        log.info("node /root/deploy/shixi/front/xybSign-node/index.js" + result);
        Result exec = commandService.exec("node /root/deploy/shixi/front/xybSign-node/index.js" + result);
        log.info("脚本运行结果：{}", JSONUtil.toJsonPrettyStr(exec));
        log.info("打卡已完成");

        return exec;
    }


    @GetMapping("/exec3/{iphoneStr}")
    @IPLimit(prefix = "batch", limit = 20, windowSize = 60)
    public Result exec3(@PathVariable("iphoneStr") String iphoneStr, HttpServletRequest request) {
        if (Objects.isNull(iphoneStr)) {
            return Result.error("501", "请选择打卡的用户~");
        }
        log.info("打卡已启动");


        List<String> iphoneList = new ArrayList<>();

        String[] split = iphoneStr.split(",");

        for (String iphone : split) {
            if (Validator.isMobile(iphone)) {
                iphoneList.add(iphone);
            }
        }



        List<User> userList = userManager.getUserListByIphoneList(iphoneList);

        StringBuilder result = new StringBuilder();

        for (User user : userList) {
            result.append(" ").append(user.getId());
        }

        log.info("node /root/deploy/shixi/front/xybSign-node/index.js" + result);
        Result exec = commandService.exec("node /root/deploy/shixi/front/xybSign-node/index.js" + result);
        log.info("脚本运行结果：{}", JSONUtil.toJsonPrettyStr(exec));
        log.info("打卡已完成");

        return exec;
//        return null;
    }

    @GetMapping("/execJob")
    public String test2() {
        List<UserListVo> userListVoList = (List<UserListVo>) userManager.userListLogs().getData();
        List<Long> userIdList = userListVoList.stream()
                // 今日未打卡成功
                .filter(item -> Boolean.FALSE.equals(item.getTodaySuccess()))
                // 启动自动签到功能
                .filter(item -> Boolean.TRUE.equals(item.getSign()))
                .map(User::getId)
                .collect(Collectors.toList());

        if (userIdList.isEmpty()) {
            return "不需要执行";
        }

        StringBuilder command = new StringBuilder("node /root/deploy/shixi/front/xybSign-node/index.js");
        for (Long userId : userIdList) {
            command.append(" ").append(userId);
        }

        commandService.exec(command.toString());
        return command.toString();
    }

}
