package resources;

import java.io.InputStream;
import java.sql.Connection;

/**
 * @author dingpei
 * @Description: 读取字节输入流
 * @date 2020/11/23 9:20 下午
 */
public class Resource {
    public static ThreadLocal<Connection> conn = new ThreadLocal<>();


    /**
     * @Description: 获取字节输入流
     * @param path 资源文件地址
     * @return java.io.InputStream
     * @Author: dingpei
     * @Date: 2020/11/23 9:22 下午
     */
    public static InputStream getResourceAsStream(String path){
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }
}
