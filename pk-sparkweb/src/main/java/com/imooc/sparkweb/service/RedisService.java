package com.imooc.sparkweb.service;

import com.imooc.sparkweb.domain.Provincecount;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedisService {

    @Resource
    RedisTemplate redisTemplate;

    public List<Provincecount> query(String day){
        List<Provincecount> Counts = new ArrayList<>();
        Map<String, String> result = getall("day-province-count" + day);
        for(Map.Entry<String,String> item: result.entrySet()){
            Provincecount row =new Provincecount();
            row.setDay(day);
            row.setProvince(item.getKey());
            row.setCount(Long.parseLong(item.getValue()));
            Counts.add(row);
        }
        return Counts;
    }
    private Map<String,String> getall(String key){

        return (Map<String, String>) redisTemplate.execute((RedisCallback<Map<String,String>>) con->{
            Map<byte[], byte[]> map = con.hGetAll(key.getBytes());

            Map<String,String> hash = new HashMap<>(map.size());
            for(Map.Entry<byte[],byte[]> entry: map.entrySet()){

                hash.put(new String(entry.getKey()),new String(entry.getValue()));

            }
            return hash;
        });

    }


}
