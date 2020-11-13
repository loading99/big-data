package com.imooc.sparkweb.service;

import com.imooc.sparkweb.domain.User;
import com.imooc.sparkweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }
    public List<User> query(){
        return userRepository.findAll();
    }
}
