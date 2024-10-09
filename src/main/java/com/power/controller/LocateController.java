package com.power.controller;

import com.power.utils.AddressUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locate")
@CrossOrigin
public class LocateController {

    @GetMapping("/getAddress")
    public String getAddress(@RequestParam("location") String location) {
        String result;
        try {
            result = AddressUtil.getAddrByLocateStr(location);
        } catch (Exception e) {
            result = "经纬度异常";
        }
        return result;
    }
}
