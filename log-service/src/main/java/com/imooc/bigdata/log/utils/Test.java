package com.imooc.bigdata.log.utils;

import com.imooc.bigdata.gen.LogGenerator;

public class Test {
    public static void main(String[] args) throws Exception{
        String url="http://hadoop000:9527/pk-web/upload";
        String code="25F5F431FBA146E0";
        LogGenerator.generator(url,code);
    }
}
