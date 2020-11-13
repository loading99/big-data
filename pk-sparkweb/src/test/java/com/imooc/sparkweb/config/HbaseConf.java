package com.imooc.sparkweb.config;

import com.imooc.sparkweb.domain.User;
import com.imooc.sparkweb.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseConf {
    @Resource
    HbaseConfig hbaseconf;
    @Test
    public void testSave(){
        System.out.println(hbaseconf);
    }
}
