package com.power.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.power.domain.ShiXiLog;
import com.power.mapper.ShiXiLogMapper;
import com.power.service.ShiXiLogService;
import org.springframework.stereotype.Service;

@Service
public class ShiXiLogServiceImpl extends ServiceImpl<ShiXiLogMapper, ShiXiLog>
        implements ShiXiLogService {
}
