package com.imooc.bigdata.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


import org.slf4j.Logger;
@Controller
public class logController {


    private static final Logger logger= LoggerFactory.getLogger(logController.class);
    @ResponseBody
    @PostMapping("/upload")
    public void upload(@RequestBody String info){
        logger.info(info);
    }
}
