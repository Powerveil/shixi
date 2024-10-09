package com.power.controller;

import cn.hutool.core.util.RuntimeUtil;
import com.power.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
@CrossOrigin
@Slf4j
public class CommandController {

    @Autowired
    private CommandService commandService;


    @GetMapping("/test")
    public String test(@RequestParam("commandStr") String commandStr) {
        log.info("运行的命令:{}", commandStr);
        String result = RuntimeUtil.execForStr(commandStr);
        log.info("运行结果:\n{}", result);
        return result;
    }


    @GetMapping("/exec")
    public String exec(@RequestParam("commandStr") String commandStr) {
        return commandService.exec(commandStr);
    }
    ///command/exec
}
