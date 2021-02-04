package beanfactory;

import invoke.ProxyHandle;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/23 9:26 下午
 */
public class DefaultBeanFactory implements BeanFactory {
    //单例池
    public static Map<String,Object> singletonObjects=new ConcurrentHashMap<>();
    //原始对象
    public static Map<String,Object> earlyObjects=new ConcurrentHashMap<>();


    //aop处理map
    public static Map<String, ProxyHandle> proxyFactories=new ConcurrentHashMap<>();


    @Override
    public void initBeanMap(InputStream inputStream) throws DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //解析字节流
        final Document read = new SAXReader().read(inputStream);
        //获取根节点
        final Element rootElement = read.getRootElement();
        //加载所有的bean标签
        final List<Element> beanList = rootElement.selectNodes("//bean");
        for (Element element : beanList) {
            //名称
            final String id = element.attributeValue("id");
            //类全限定路径
            final String classPath = element.attributeValue("class");
            final Class<?> aClass = Class.forName(classPath);
            final Object o = aClass.newInstance();
            //放入对象池中
            singletonObjects.put(id,o);
        }
        //解析依赖关系
        final List<Element> propertyList = rootElement.selectNodes("//property");
        for (Element element : propertyList) {
            String name=element.attributeValue("name");
            String ref=element.attributeValue("ref");
            //父节点
            final String parentId = element.getParent().attributeValue("id");
            //获取对象
            final Object parentObject = singletonObjects.get(parentId);
            //反射获取所有方法
            final Method[] methods = parentObject.getClass().getMethods();
            //遍历所有方法
            for (Method method : methods) {
                //找到属性的set方法
                if (method.getName().equalsIgnoreCase("set"+name)){
                    //获取对象
                    final Object propertyObject = singletonObjects.get(ref);
                    //反射执行方法
                    method.invoke(parentObject,propertyObject);
                }
            }
            //将维护好依赖的对象重新放入map集合
            singletonObjects.put(parentId,parentObject);
        }

    }

    @Override
    public <T> T getBean(String string) {
        return (T) singletonObjects.get(string);
    }
}
