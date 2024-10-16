package com.power.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.power.domain.vo.Result;
import com.power.limit.SlidingWindowRateLimiter;
import com.power.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/command")
@CrossOrigin
@Slf4j
public class CommandController {

    @Autowired
    private CommandService commandService;

    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Value("${me.whiteIPList}")
    private List<String> whiteIPList;


    @GetMapping("/test")
    public String test(@RequestParam("commandStr") String commandStr) {
        log.info("运行的命令:{}", commandStr);
        String result = RuntimeUtil.execForStr(commandStr);
        log.info("运行结果:\n{}", result);
        return result;
    }

    @PostMapping("/exec")
    public Result exec(@RequestBody(required = false) List<Long> idList, HttpServletRequest request) {
        String remoteHost = request.getRemoteHost();
        if (!whiteIPList.contains(remoteHost)) {
            Boolean access = slidingWindowRateLimiter.tryAcquire("batchCommand." + remoteHost, 5, 60);
            if (!access) {
                return Result.error("501", "操作次数太多了，请稍后重试！");
            }
        }

        StringBuilder result = new StringBuilder();
        for (Long l : idList) {
            result.append(" ").append(l);
        }
        return commandService.exec("node /root/deploy/shixi/test/xybSign-node/index.js" + result);
    }
    ///command/exec
}
