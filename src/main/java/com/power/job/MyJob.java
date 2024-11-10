package com.power.job;

import com.power.domain.User;
import com.power.domain.vo.Result;
import com.power.domain.vo.UserListVo;
import com.power.manager.UserManager;
import com.power.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MyJob {

    @Autowired
    private CommandService commandService;

    @Autowired
    private UserManager userManager;

//    @Scheduled(cron = "0 0 8 * * ?")
//    public void testSchedule() {
//        commandService.exec("node /root/deploy/shixi/test/xybSign-node/index.js");
//    }


    @Scheduled(cron = "0 0/30 8-14 * * ?")
    public void testSchedule2() {
        log.info("定时任务已启动");
        List<UserListVo> userListVoList = (List<UserListVo>) userManager.userListLogs().getData();
        List<Long> userIdList = userListVoList.stream()
                // 今日未打卡成功
                .filter(item -> Boolean.FALSE.equals(item.getTodaySuccess()))
                // 启动自动签到功能
                .filter(item -> Boolean.TRUE.equals(item.getSign()))
                .map(User::getId)
                .collect(Collectors.toList());

        if (userIdList.isEmpty()) {
            log.info("没有任务执行！");
            return;
        }

        StringBuilder command = new StringBuilder("node /root/deploy/shixi/test/xybSign-node/index.js");
        for (Long userId : userIdList) {
            command.append(" ").append(userId);
        }
        Result result = commandService.exec(command.toString());
        log.info("脚本运行结果：{}", result);
        log.info("定时任务已结束");
    }

}
