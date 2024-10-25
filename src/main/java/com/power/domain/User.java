package com.power.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 微信小程序抓包openid
     */
    private String openId;

    /**
     * 微信小程序抓包unionId
     */
    private String unionId;

    /**
     * 是否自动签到
     */
    private Boolean sign;

    /**
     * 是否重新签到
     */
    private Boolean reSign;

    /**
     * 经纬度
     */
    private String location;

    /**
     * 地理位置
     */
    private String address;

    /**
     * 签到图片
     */
    private String signImagePath;

    /**
     * 是否自动填写周报
     */
    private Boolean needReport;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Boolean delFlag;
}
