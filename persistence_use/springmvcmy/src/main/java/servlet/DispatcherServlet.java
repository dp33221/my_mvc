package servlet;


import annotations.MyRequestMapping;
import beanfactory.DefaultBeanFactory;
import handler.Handle;
import invoke.MySecurityInvokeHandle;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import scanner.ClassScanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static beanfactory.DefaultBeanFactory.earlyObjects;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/12/9 10:15 下午
 */
public class DispatcherServlet extends HttpServlet {
    private static Map<String,Handle> handlerMapping = new HashMap<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求路径
        final String requestURI = req.getRequestURI();
        //查询过映射器
        final Handle handle = handlerMapping.get(requestURI);
        if (handle == null) {
            resp.getWriter().write("404");
            return;
        }
        //获取请求参数  数量
        final Parameter[] parameters = handle.getMethod().getParameters();
        //创建参数数组
        Object[] paramterArry = new Object[parameters.length];
        //获取请求参数
        final Map<String, String[]> parameterMap = req.getParameterMap();
        //参数顺序
        final Map<String, Integer> pamaterMap = handle.getPamaterMap();
        //遍历
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            //合并相同的名称参数
            final String join = StringUtils.join(entry.getValue(), ",");
            if (!pamaterMap.containsKey(entry.getKey())) {
                continue;
            }
            //属性index
            final Integer index = pamaterMap.get(entry.getKey());
            paramterArry[index] = join;

        }
        //设置request以及response参数
        if(pamaterMap.containsKey(HttpServletRequest.class.getSimpleName())){
            final Integer requestIndex = pamaterMap.get(HttpServletRequest.class.getSimpleName());
            paramterArry[requestIndex]=req;

        }
        if(pamaterMap.containsKey(HttpServletResponse.class.getSimpleName())){
            final Integer responseIndex = pamaterMap.get(HttpServletResponse.class.getSimpleName());
            paramterArry[responseIndex]=resp;

        }


        //执行目标方法
        try {
            final Object invoke = handle.getMethod().invoke(handle.getController(), paramterArry);
            resp.setCharacterEncoding("GBK");
            resp.getWriter().write(invoke.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @SneakyThrows
    @Override
    public void init() throws ServletException {
        new MySecurityInvokeHandle();
        //启动myspring扫描
        new ClassScanner().initObject();
        //初始化handlermapping
        //1.遍历ioc容器
        final Map<String, Object> singletonObjects = DefaultBeanFactory.singletonObjects;
        for(Map.Entry<String,Object> entry:singletonObjects.entrySet()){
//            Class<?> aClass=entry.getValue().getClass();
            final Object o = earlyObjects.get(entry.getKey());
            if(o==null){
                continue;
            }
            Class<?> aClass=o.getClass();
            String baseUrl="";
            //获取类上的url
            if(aClass.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping annotation = aClass.getAnnotation(MyRequestMapping.class);
                baseUrl = annotation.value();
            }
            final Method[] methods = aClass.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method=methods[i];
                //获取注解
                final MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
                if(myRequestMapping ==null){
                    continue;
                }
                final String value = myRequestMapping.value();
                String url=baseUrl+value;
                //封装信息
                final Handle handle = Handle.builder().method(method).controller(entry.getValue()).url(url).build();
                final Parameter[] parameters = method.getParameters();

                for (int j = 0; j < parameters.length; j++) {
                    final Parameter parameter = parameters[j];
                    //如果是httprequest或者httpresponse则直接存储名称
                    if(parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class) {
                        handle.getPamaterMap().put(parameter.getType().getSimpleName(),j) ;
                    }else{
                        handle.getPamaterMap().put(parameter.getName(),j);
                    }
                }
                //添加到映射器中
                handlerMapping.put(url,handle);

            }

        }
    }


}
