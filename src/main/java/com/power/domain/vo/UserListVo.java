package com.power.domain.vo;

import com.power.domain.ShiXiLog;
import com.power.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserListVo extends User {
    private Boolean todaySuccess;
    private List<ShiXiLog> shiXiLogList;
}
