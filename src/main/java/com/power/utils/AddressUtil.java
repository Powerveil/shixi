package com.power.utils;
 
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
 
public class AddressUtil {
    private static final Logger log = LoggerFactory.getLogger(AddressUtil.class);

    public static String getAddrByLocateStr(String location) {
        String[] split = location.split(",");
        return getAddrByLatAndLng(split[0], split[1]).toString();
    }
 
    private static JSONObject getLatAndLngByAddr(String longitude, String latitude) {
        try {
            String queryStr ="https://www.amap.com/service/regeo?longitude="+longitude+"&latitude="+latitude+"";
            String info = HttpUtil.get(queryStr);
            if (StrUtil.isNotBlank(info)) {
                JSONObject resultJson = JSONUtil.parseObj(info);
                JSONObject dataJson = resultJson.getJSONObject("data");
                return dataJson;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String getItab() {
        String itabUrl = "https://v2.jinrishici.com/one.json?client=browser-sdk/1.2&X-User-Token=VhhFS6SJRAmbqePEOU0%2B0f1%2BCmFLxkFo";
        String info = HttpUtil.get(itabUrl);
        if (StrUtil.isNotBlank(info)) {
            JSONObject resultJson = JSONUtil.parseObj(info);
            JSONObject origin = resultJson.getJSONObject("origin");
            String content = origin.getStr("content");
            return content;
        } else {
            return null;
        }
    }
    
    public static String getAddrByLatAndLng(String longitude, String latitude) {
        if (StrUtil.isBlank(longitude) || StrUtil.isBlank(latitude)) {
            return "经纬度不能为空";
        }
        JSONObject latAndLngByAddr = getLatAndLngByAddr(longitude, latitude);

//        List<JSONObject> poiList = latAndLngByAddr.getJSONArray("poi_list").toList(JSONObject.class);
//        String address = poiList.get(0).getStr("address");

        return Convert.convert(String.class, latAndLngByAddr.get("desc")).replaceAll(",", "-") + "-" + Convert.convert(String.class, latAndLngByAddr.get("pos"));
    }
 
    public static void main(String[] args) {
//        String addrByLatAndLng = getAddrByLatAndLng("116.481488", "39.990464");
        String addrByLatAndLng = getAddrByLatAndLng("113.56", "34.83");
        System.out.println(addrByLatAndLng);
    }
}