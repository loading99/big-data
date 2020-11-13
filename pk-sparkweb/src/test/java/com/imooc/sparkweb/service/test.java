package com.imooc.sparkweb.service;

import com.imooc.sparkweb.domain.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Resource
    UserService userService;
    @Test
    public void testSave(){
        for (int i=0;i<10;i++){
            User user = new User( i,"pk", "pk" + i + "mail");
            userService.save(user);
        }
    }

    @Test
    public void testQuery(){
        List<User> users = userService.query();
        for (User user: users){
            System.out.println(user);
        }

    }
}
