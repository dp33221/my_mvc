package excutor;

import config.Configuration;
import config.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dingpei
 * @Description: 执行器接口
 * @date 2020/11/11 9:33 下午
 */
public interface Excutor {
    //查询
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement,Object...params) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;
    //增删改
    int update(Configuration configuration, MappedStatement mappedStatement, Parameter[] parameters, Object...params) throws SQLException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException;
}
