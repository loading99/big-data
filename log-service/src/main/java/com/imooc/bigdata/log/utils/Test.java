package com.imooc.bigdata.log.utils;

import com.imooc.bigdata.gen.LogGenerator;

public class Test {
    public static void main(String[] args) throws Exception{
        String url="http://hadoop000:9527/pk-web/upload";
        String code="123456"; //The password is intentionally disguised, you can use your own data source
        LogGenerator.generator(url,code);
    }
}
