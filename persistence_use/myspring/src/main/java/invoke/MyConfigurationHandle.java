package invoke;

import annotation.MyBean;
import annotation.MyConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static beanfactory.DefaultBeanFactory.proxyFactories;
import static beanfactory.DefaultBeanFactory.singletonObjects;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/29 9:53 下午
 */
public class MyConfigurationHandle implements ProxyHandle {
    static {
        proxyFactories.put(MyConfiguration.class.getName(),new MyConfigurationHandle());
    }
    @Override
    public Object JdkDynamic(Object e) {
        return getProxy(e);
    }

    @Override
    public Object CgLibDynamic(Object e) throws InvocationTargetException, IllegalAccessException {
        return getProxy(e);
    }

    public  Object getProxy(Object e){
        final Method[] methods = e.getClass().getMethods();
        for (Method method : methods) {
            if(method.getAnnotation(MyBean.class)==null){
                continue;
            }
            try {
                final Object invoke = method.invoke(e);
                final String name = method.getName();
                singletonObjects.put(name,invoke);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return e;
    }
}
