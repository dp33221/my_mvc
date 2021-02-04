package beanfactory;

import org.dom4j.DocumentException;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/23 9:23 下午
 */
public interface BeanFactory {
    void initBeanMap(InputStream inputStream) throws DocumentException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException;
    <T> T getBean(String string);
}
