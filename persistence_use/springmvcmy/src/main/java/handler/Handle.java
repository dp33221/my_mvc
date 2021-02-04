package handler;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/12/10 4:57 下午
 */
@Data
@Builder
public class Handle {
    //实现类 调用方法的对象
    private Object controller;
    //对应的方法
    private Method method;
    //请求路径
    private String url;
    //参数index
    @Builder.Default
    private Map<String,Integer> pamaterMap=new HashMap<>();
}
