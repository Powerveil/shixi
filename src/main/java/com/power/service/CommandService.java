package com.power.service;

import com.power.domain.vo.Result;

public interface CommandService {
    Result exec(String commandStr);
}
