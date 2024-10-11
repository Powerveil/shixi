package com.power.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONUtil;
import com.power.domain.ShiXiLog;
import com.power.domain.vo.Result;
import com.power.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandServiceImpl implements CommandService {
    @Override
    public Result exec(String commandStr) {
        try {
            log.info("运行的命令:{}", commandStr);
            String result = RuntimeUtil.execForStr(commandStr);
            log.info("运行结果:\n{}", result);
            List<ShiXiLog> list = JSONUtil.toList(result, ShiXiLog.class);
            // 返回脚本执行失败的userIdList
            List<String> userIdList = list.stream().filter(item -> Boolean.FALSE.equals(item.getSuccess()))
                    .map(item -> {
                        String username = item.getUsername();
                        return username.substring(username.length() - 4);
                    })
                    .collect(Collectors.toList());

            return Result.success(userIdList);
        } catch (IORuntimeException e) {
            return Result.error("501", "脚本执行失败，请管理员查看日志！");
        }
    }

}
