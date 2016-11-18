package com.sunzequn.srm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 读取Properties配置文件的工具类
 */
public class PropertiesUtil {

    private Properties properties = new Properties();

    public PropertiesUtil(String file) {
        try {
            InputStream inputStream = new FileInputStream(new File(file));
            this.properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从配置文件中根据键获得值
     *
     * @param key 键
     * @return 如果键值对存在则返回值, 否则返回null
     */
    public String getValue(String key) {
        return properties.getProperty(key);
    }
}
