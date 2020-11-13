package com.imooc.sparkweb.controller;


import com.imooc.sparkweb.domain.User;
import com.imooc.sparkweb.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/query")
    public List<User> query(){

        List<User> users = userService.query();
        for (User user: users){
            System.out.println(user);
        }
        return userService.query();
    }
}
