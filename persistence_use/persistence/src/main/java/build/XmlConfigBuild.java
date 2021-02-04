package build;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import config.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import resources.Resources;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author dingpei
 * @Description: 解析sqlmapconfig.xml
 * @date 2020/11/10 7:04 下午
 */
public class XmlConfigBuild {
    private Configuration configuration;
    public XmlConfigBuild(){
        this.configuration=new Configuration();
    }

    /**
     * @Description: 解析sqlMapConfig文件 封装成Configuration对象
     * @param inputStream 字节输入流
     * @return config.Configuration
     * @Author: dingpei
     * @Date: 2020/11/10 8:14 下午
     */
    public Configuration buildConfiguration(InputStream inputStream) throws DocumentException, PropertyVetoException, ClassNotFoundException {
        //解析字节流
        Document document=new SAXReader().read(inputStream);
        //获取根标签
        final Element rootElement = document.getRootElement();
        //获取所有property配置
        final List<Element> propertyList = rootElement.selectNodes("//property");
        //存储解析的k v
        Properties resources=new Properties();
        for (Element element : propertyList) {
            final String name = element.attributeValue("name");
            final String value = element.attributeValue("value");
            resources.put(name,value);
        }
        //创建链接池
        ComboPooledDataSource comboPooledDataSource=new ComboPooledDataSource();
        comboPooledDataSource.setJdbcUrl(resources.getProperty("jdbcUrl"));
        comboPooledDataSource.setDriverClass(resources.getProperty("driverClass"));
        comboPooledDataSource.setUser(resources.getProperty("username"));
        comboPooledDataSource.setPassword(resources.getProperty("password"));
        //设置数据源
        configuration.setDataSource(comboPooledDataSource);
        XmlMapperBuild xmlMapperBuild=new XmlMapperBuild(configuration);
        //加载mapper.xml
        final List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            final String mapperPath = element.attributeValue("resource");
            InputStream in= Resources.getResourcesAsStream(mapperPath);
            xmlMapperBuild.build(in);
        }
        return configuration;

    }
}
