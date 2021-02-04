import beanfactory.DefaultBeanFactory;
import org.dom4j.DocumentException;
import org.junit.Test;
import resources.Resources;
import scanner.ClassScanner;
import service.IMySimpleSpringService;
import service.MySimpleSpringServiceCgLib;
import sqlsession.SqlSessionFactoryBuild;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/23 10:13 下午
 */
public class MySpringTest {


    @Test
    public void test() throws ClassNotFoundException, InvocationTargetException, InstantiationException, DocumentException, IllegalAccessException, PropertyVetoException {
        final InputStream resourcesAsStream = Resources.getResourcesAsStream("application.xml");
        final DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        defaultBeanFactory.initBeanMap(resourcesAsStream);
        SqlSessionFactoryBuild sqlSessionFactoryBuild = defaultBeanFactory.getBean("sqlSessionFactoryBuild");

        System.out.println(sqlSessionFactoryBuild);


    }
    @Test
    public void test1() throws Exception {
        new ClassScanner().initObject();
        final MySimpleSpringServiceCgLib mySimpleSpringService  = new DefaultBeanFactory().getBean("mySimpleSpringServiceCgLib");
        mySimpleSpringService.test();

    }

    @Test
    public void test2() throws Exception {
        new ClassScanner().initObject();
        final IMySimpleSpringService mySimpleSpringService  = new DefaultBeanFactory().getBean("mySimpleSpringServiceJdk");
        mySimpleSpringService.test();

    }






}
