package sqlsession;

import config.Configuration;
import config.MappedStatement;
import excutor.DefaultExcutor;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dingpei
 * @Description: sql会话对象
 * @date 2020/11/10 6:50 下午
 */
public class DefaultSqlSession implements SqlSession {
    public DefaultSqlSession(){

    }

    private Configuration configuration;
    public DefaultSqlSession(Configuration configuration){
        this.configuration=configuration;
    }


    @Override
    public <E> List<E> selectAll(String statementId, Object... params) throws NoSuchFieldException, SQLException, InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        final MappedStatement mappedStatement = configuration.getMappedStatementList().get(statementId);
        return new DefaultExcutor().query(configuration,mappedStatement,params);
    }

    @Override
    public <E> E selectOne(String statementId, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        final List<Object> objects = selectAll(statementId, params);
        if(objects.size()==0){
            return (E) new Object();
        }
        if(objects.size()>1){
            throw new RuntimeException("out of 2");
        }
        return (E) objects.get(0);
    }

    public Integer update(String statementId,Parameter[] parameters, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        final MappedStatement mappedStatement = configuration.getMappedStatementList().get(statementId);
        return new DefaultExcutor().update(configuration,mappedStatement,parameters,params);
    }

    @Override
    public <E> E getMapper(Class c) {
        final Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{c}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                //获取类全限定名+方法名
                final String methodName = method.getName();
                final String className = method.getDeclaringClass().getName();
                String statementId=className+methodName;
                final MappedStatement mappedStatement = configuration.getMappedStatementList().get(statementId);
                final Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof ParameterizedType){
                    return selectAll(statementId,objects);
                }
                //如果不是基本数据类型则是查询单个
                else if(!(genericReturnType.getClass().getClassLoader() ==null)|| "select".equals(mappedStatement.getExecutorType())) {
                    return selectOne(statementId,objects);

                }

                //获取方法的型参名
                final Parameter[] parameters = method.getParameters();
                return update(statementId,parameters,objects);
            }
        });
        return (E) o;

    }
}
