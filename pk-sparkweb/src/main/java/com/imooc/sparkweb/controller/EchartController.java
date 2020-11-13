package com.imooc.sparkweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EchartController {

    @GetMapping("/echarts")
    public String echarts(){
        return "demo";
    }
}
