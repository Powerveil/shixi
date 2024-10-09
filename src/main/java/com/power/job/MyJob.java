package com.power.job;

import com.power.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyJob {

    @Autowired
    private CommandService commandService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void testSchedule() {
        commandService.exec("node /root/deploy/shixi/test/xybSign-node/index.js");
    }

}
