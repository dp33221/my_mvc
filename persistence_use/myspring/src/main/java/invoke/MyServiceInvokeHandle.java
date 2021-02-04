package invoke;

import annotation.MyService;
import beanfactory.DefaultBeanFactory;
import utils.HelpUtils;

import static beanfactory.DefaultBeanFactory.earlyObjects;
import static beanfactory.DefaultBeanFactory.proxyFactories;

/**
 * @author dingpei
 * @Description: service动态代理
 * @date 2020/11/24 9:39 下午
 */
public class MyServiceInvokeHandle implements ProxyHandle{
    //初始化放入map
    static {
        proxyFactories.put(MyService.class.getName(),new MyServiceInvokeHandle());
    }

    @Override
    public Object JdkDynamic(Object e){
        return getProxy(e);
    }

    @Override
    public Object CgLibDynamic(Object e){
        String className = HelpUtils.toLowerCaseFirstOne(e.getClass().getSimpleName());
        final MyService annotation = e.getClass().getAnnotation(MyService.class);
        final String value = annotation.value();
        if(!"".equalsIgnoreCase(value)){
            className=value;
        }
        DefaultBeanFactory.singletonObjects.put(className,e);

        earlyObjects.put(className,e);

        return e;
    }

    public Object getProxy(Object e){
        String className = HelpUtils.toLowerCaseFirstOne(e.getClass().getSimpleName());
        final MyService annotation = e.getClass().getAnnotation(MyService.class);
        final String value = annotation.value();
        if(!"".equalsIgnoreCase(value)){
            className=value;
        }
        final Class<?>[] interfaces = e.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            DefaultBeanFactory.singletonObjects.put(HelpUtils.toLowerCaseFirstOne(anInterface.getSimpleName()),e);
        }
        DefaultBeanFactory.singletonObjects.put(className,e);
        earlyObjects.put(className,e);

        return e;
    }

}
