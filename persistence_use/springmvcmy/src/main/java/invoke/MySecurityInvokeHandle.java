package invoke;

import annotations.MySecurity;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

import static beanfactory.DefaultBeanFactory.proxyFactories;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/12/11 10:37 上午
 */
public class MySecurityInvokeHandle implements ProxyHandle {
    static {
        proxyFactories.put(MySecurity.class.getName(),new MySecurityInvokeHandle());
    }
    @Override
    public Object JdkDynamic(Object e) {
        return null;
    }

    @Override
    public Object CgLibDynamic(Object e) throws InvocationTargetException, IllegalAccessException {
        return Enhancer.create(e.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                final MySecurity mySecurity = e.getClass().getAnnotation(MySecurity.class);
                final HashSet<Object> set = new HashSet<>(Arrays.asList(objects));

                //类上注解
                if(mySecurity!=null){
                    final String[] values = mySecurity.value();
                    for(String value:values){
                        if(set.contains(value)){
                            return "没有权限";
                        }
                    }

                }
                //方法上注解
                final MySecurity annotation = method.getAnnotation(MySecurity.class);
                if(annotation!=null){
                    final String[] values = annotation.value();
                    for(String value:values){
                        if(set.contains(value)){
                            return "没有权限";
                        }
                    }

                }

                return method.invoke(e,objects);
            }
        });
    }
}
