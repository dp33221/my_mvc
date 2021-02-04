import mapper.PaymentChannelMapper;
import org.dom4j.DocumentException;
import org.junit.Test;
import pojo.PaymentChannel;
import resources.Resources;
import sqlsession.DefaultSqlSession;
import sqlsession.SqlSessionFactory;
import sqlsession.SqlSessionFactoryBuild;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/11 2:06 下午
 */
public class MyPersistenceTest {

    @Test
    public void test() throws DocumentException, PropertyVetoException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        final InputStream sqlMapConfig = Resources.getResourcesAsStream("sqlMapConfig.xml");
        final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuild().build(sqlMapConfig);
        final DefaultSqlSession defaultSqlSession = sqlSessionFactory.openSession();
        final List<PaymentChannel> paymentChannels = defaultSqlSession.selectAll("userselectAll");
        for (PaymentChannel paymentChannel:paymentChannels ){
            System.out.println(paymentChannel);
        }
    }

    @Test
    public void test1() throws DocumentException, PropertyVetoException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        final InputStream sqlMapConfig = Resources.getResourcesAsStream("sqlMapConfig.xml");
        final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuild().build(sqlMapConfig);
        final DefaultSqlSession defaultSqlSession = sqlSessionFactory.openSession();
        final PaymentChannelMapper paymentChannelMapper = defaultSqlSession.getMapper(PaymentChannelMapper.class);

        final List<PaymentChannel> paymentChannels = paymentChannelMapper.selectAll();
        for (PaymentChannel paymentChannel:paymentChannels){
            System.out.println(paymentChannel);
        }
        final PaymentChannel paymentChannel = paymentChannelMapper.selectOne(PaymentChannel.builder().id(1).channel_name("微信支付").build());
        System.out.println("查询单个-------");
        System.out.println(paymentChannel);
    }

    @Test
    public void test3() throws DocumentException, PropertyVetoException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        final InputStream sqlMapConfig = Resources.getResourcesAsStream("sqlMapConfig.xml");
        final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuild().build(sqlMapConfig);
        final DefaultSqlSession defaultSqlSession = sqlSessionFactory.openSession();
        final PaymentChannelMapper paymentChannelMapper = defaultSqlSession.getMapper(PaymentChannelMapper.class);

        final int res = paymentChannelMapper.update(999, "测试支付2");
        System.out.println(res);

    }

    @Test
    public void test4() throws DocumentException, PropertyVetoException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        final InputStream sqlMapConfig = Resources.getResourcesAsStream("sqlMapConfig.xml");
        final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuild().build(sqlMapConfig);
        final DefaultSqlSession defaultSqlSession = sqlSessionFactory.openSession();
        final PaymentChannelMapper paymentChannelMapper = defaultSqlSession.getMapper(PaymentChannelMapper.class);

        final int res = paymentChannelMapper.insert(PaymentChannel.builder().id(999).channel_name("测试支付").build());
        System.out.println(res);

    }

    @Test
    public void test5() throws DocumentException, PropertyVetoException, ClassNotFoundException, IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        final InputStream sqlMapConfig = Resources.getResourcesAsStream("sqlMapConfig.xml");
        final SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuild().build(sqlMapConfig);
        final DefaultSqlSession defaultSqlSession = sqlSessionFactory.openSession();
        final PaymentChannelMapper paymentChannelMapper = defaultSqlSession.getMapper(PaymentChannelMapper.class);

        final int res = paymentChannelMapper.delete(999);
        System.out.println(res);

    }



}
