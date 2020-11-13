package com.imooc.sparkweb.service;


import com.imooc.sparkweb.domain.AccessHour;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Hbasetest {
    @Resource
    HbaseService hbaseService;

    @Test
    public void query(){
        List<AccessHour> accessHours= hbaseService.query("access_user_hour","2020110616","2020110617");
        for (AccessHour domain:accessHours){
            System.out.println(domain);
        }
    }


}
