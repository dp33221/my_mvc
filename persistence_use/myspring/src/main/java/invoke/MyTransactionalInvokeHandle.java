package invoke;

import annotation.MyTransactional;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import resources.Resource;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import static beanfactory.DefaultBeanFactory.proxyFactories;

/**
 * @author dingpei
 * @Description: transactional注解动态代理
 * @date 2020/11/24 10:18 下午
 */
public class MyTransactionalInvokeHandle implements ProxyHandle{
    //初始化放入map
    static {
        proxyFactories.put(MyTransactional.class.getName(),new MyTransactionalInvokeHandle());
    }
    @Override
    public Object JdkDynamic(Object e){
        return getProxy(e);
    }



    public  Object getProxy(Object e){
        return Proxy.newProxyInstance(MyTransactionalInvokeHandle.class.getClassLoader(), e.getClass().getInterfaces(), (o1, method, objects) -> {
            final Method method1 = e.getClass().getMethod(method.getName(),method.getParameterTypes());

            final MyTransactional annotation = method1.getAnnotation(MyTransactional.class);
            if(annotation!=null){
                final Connection connection = Resource.conn.get();
                connection.setAutoCommit(false);
                try{
                    final Object invoke = method.invoke(e, objects);
                    connection.commit();
                    return invoke;
                }catch (Exception e1){
                    connection.rollback();
                    throw e1;
                }
                finally {
                    connection.close();
                }
            }
            return method.invoke(e,objects);
        });
    }

    @Override
    public Object CgLibDynamic(Object e){
       return Enhancer.create(e.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                final MyTransactional annotation = method.getAnnotation(MyTransactional.class);
                if(annotation!=null){
                    final Connection connection = Resource.conn.get();

                    connection.setAutoCommit(false);
                    try{
                        final Object invoke = method.invoke(e, objects);
//                final Object invoke = methodProxy.invokeSuper(o, objects);
                        connection.commit();
                        return invoke;
                    }catch (Exception e1){
                        connection.rollback();
                        throw e1;
                    }
                    finally {
                        connection.close();
                    }
                }

                return method.invoke(e, objects);
            }
        });
    }

}
