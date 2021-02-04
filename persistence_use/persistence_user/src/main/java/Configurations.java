import annotation.MyBean;
import annotation.MyConfiguration;
import mapper.PaymentChannelMapper;
import org.dom4j.DocumentException;
import resources.Resources;
import sqlsession.DefaultSqlSession;
import sqlsession.SqlSessionFactory;
import sqlsession.SqlSessionFactoryBuild;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/29 9:44 下午
 */
@MyConfiguration
public class Configurations {
    @MyBean
    public Object paymentChannelMapper() throws DocumentException, PropertyVetoException, ClassNotFoundException {
        final InputStream sqlMapConfig = Resources.getResourcesAsStream("sqlMapConfig.xml");

        final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuild().build(sqlMapConfig);
        final DefaultSqlSession defaultSqlSession = sqlSessionFactory.openSession();
        return defaultSqlSession.getMapper(PaymentChannelMapper.class);
    }

}
