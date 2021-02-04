package resources;

import java.io.InputStream;

/**
 * @author dingpei
 * @Description: 资源加载类
 * @date 2020/11/10 4:39 下午
 */
public class Resources {

    /**
     * @Description: 获取资源转化为字节输入流
     * @param path 资源路径
     * @return java.io.InputStream
     * @Author: dingpei
     * @Date: 2020/11/10 4:41 下午
     */
    public static InputStream getResourcesAsStream(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

}
