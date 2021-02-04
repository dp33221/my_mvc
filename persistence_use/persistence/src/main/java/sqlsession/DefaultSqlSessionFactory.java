package sqlsession;

import config.Configuration;

/**
 * @author dingpei
 * @Description: 默认sqlsession工厂
 * @date 2020/11/10 6:49 下午
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration){
        this.configuration=configuration;
    }
    @Override
    public DefaultSqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
