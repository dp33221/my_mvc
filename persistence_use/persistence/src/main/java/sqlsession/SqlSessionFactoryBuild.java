package sqlsession;

import beanfactory.DefaultBeanFactory;
import build.XmlConfigBuild;
import config.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/10 6:47 下午
 */
public class SqlSessionFactoryBuild {

    private Configuration configuration;

    public SqlSessionFactoryBuild(){
        this.configuration=new Configuration();
    }

    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException, ClassNotFoundException {
        //解析mapper和SqlMapConfig
        final Configuration configuration = new XmlConfigBuild().buildConfiguration(in);
        DefaultBeanFactory.singletonObjects.put("dateSource",configuration.getDataSource());
        //创建sqlsessionfactory
        return new DefaultSqlSessionFactory(configuration);

    }
}
