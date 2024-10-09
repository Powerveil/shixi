package com.power.controller;

import com.power.domain.User;
import com.power.domain.vo.Result;
import com.power.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserManager userManager;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/list")
    //todo 加状态
    public Result userList(@RequestBody(required = false) List<Long> idList) {
        return userManager.userList(idList);
    }

    @GetMapping("/listLogs")
    //todo 加状态
    public Result userListLogs() {
        return userManager.userListLogs();
    }

    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        return userManager.addUser(user);
    }

    @PutMapping("/modify")
    public Result modifyUser(@RequestBody User user) {
        return userManager.modifyUser(user);
    }

    @DeleteMapping("/remove/{userId}")
    public Result removeUser(@PathVariable("userId") Long userId) {
        return userManager.removeUser(userId);
    }
}
