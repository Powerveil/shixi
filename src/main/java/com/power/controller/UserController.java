package com.power.controller;

import com.power.annotation.MyLimit;
import com.power.domain.User;
import com.power.domain.vo.Result;
import com.power.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
@MyLimit(prefix = "user")
public class UserController {

    @Autowired
    private UserManager userManager;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/listByJob")
    public Result userList(@RequestBody(required = false) List<Long> idList) {
        return userManager.userList(idList);
    }

    @GetMapping("/listLogs")
    public Result userListLogs() {
        return userManager.userListLogs();
    }

    @GetMapping("/getById/{userId}/{password}")
    public Result getById(@PathVariable("userId") Long userId,
                          @PathVariable("password") String password) {
        return userManager.getById(userId, password);
    }

//    @GetMapping("/getToken/{userId}/{password}")
//    public Result getToken(@PathVariable("userId") Long userId,
//                           @PathVariable("password") String password) {
//        return userManager.getToken(userId, password);
//    }

    @PostMapping("/add")
    @MyLimit(prefix = "add")
    public Result addUser(@RequestBody User user) {
        return userManager.addUser(user);
    }

    @PutMapping("/modify")
    public Result modifyUser(@RequestBody User user) {
        return userManager.modifyUser(user);
    }

    @DeleteMapping("/remove/{userId}/{password}")
    public Result removeUser(@PathVariable("userId") Long userId,
                             @PathVariable("password") String password) {
        return userManager.removeUser(userId, password);
    }

    @GetMapping("/jntm")
    public Result getAdminToken() {
        return userManager.getAdminToken();
    }
}
