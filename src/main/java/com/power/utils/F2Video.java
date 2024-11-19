package com.power.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class F2Video {


    static Map<String, String> map = new LinkedHashMap<>();


    static {
        map.put("https://v.douyin.com/iAnU6xLT", "我不是医生");
        map.put("https://v.douyin.com/iAndxtH8", "小Lin说");
        map.put("https://v.douyin.com/iAtouCdo", "阳老讲堂");
        map.put("https://v.douyin.com/iAtkgAXx", "银行小土豆");
        map.put("https://v.douyin.com/iAcf86qH", "未小知");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

//        map.forEach((k, v) -> {
//            System.out.println("==========================================================");
//            String commandStr = "f2 dy -M post -u " + k;
//            System.out.println("正在操作：" + v);
//            String result = RuntimeUtil.execForStr(commandStr);
//            if (result.contains("所有作品采集完毕")) {
//                System.out.println("执行成功😄");
//            } else {
//                System.out.println("执行失败🙁");
//            }
//        });


        System.out.println("==========================================================");
        String commandStr = "f2 dy -M post -u " + "https://v.douyin.com/iAcPGG4U";
        System.out.println("正在操作：" + "武主任【企业综合办】");
        System.out.println("操作命令：" + commandStr);

//        System.out.println(CharsetUtil.systemCharset());
        String result = RuntimeUtil.execForStr(commandStr);
        if (result.contains("所有作品采集完毕")) {
            System.out.println("执行成功");
        }
//        System.out.println("操作结果：\n" + result);

//        System.out.println(Arrays.toString(commandStr.getBytes()));
//        System.out.println(Arrays.toString(commandStr.getBytes("gbk")));
//        String result = new String(RuntimeUtil.execForStr(commandStr).getBytes(StandardCharsets.UTF_8), "gbk");
//        result = new String(result.getBytes("gbk"), StandardCharsets.UTF_8);
//        System.out.println("操作结果：\n" + result);

//        String s = RuntimeUtil.execForStr("ipconfig");
//        System.out.println(s);


    }
}
