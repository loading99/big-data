package com.imooc.sparkweb.service;

import com.imooc.sparkweb.domain.Provincecount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Resource
    RedisService redisService;

    @Test
    public void query(){
        List<Provincecount> values = redisService.query("20201103");
        for (Provincecount item: values){

            System.out.println(item);
        }
    }
}
