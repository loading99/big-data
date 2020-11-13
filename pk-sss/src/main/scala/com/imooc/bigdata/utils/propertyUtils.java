package com.imooc.bigdata.utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description: 主要完成了properties文件的操作工具类
 *
 * @Author: Garrett Wang
 * @Date: 2019/11/23 17:15
 * @Modified By：
 */
public final class propertyUtils {

    // 当前class的日志公共对象
    private static final Logger LOGGER = Logger.getLogger(propertyUtils.class);
    /**
     * @Description 获取某一个properties文件的properties对象，以方便或得文件里面的值
     *
     * @Author Garrett Wang
     * @param filePath：properties文件所在路径
     * @return Properties对象
     */
    public static Properties conf(String filePath) {

        final Properties properties = new Properties();

        try {
            properties.load(propertyUtils.class.getClassLoader().getResourceAsStream(filePath));
        } catch (IOException e) {
            LOGGER.error("获取某一个properties文件的properties对象失败", e);
        }

       return properties;
    }


}


