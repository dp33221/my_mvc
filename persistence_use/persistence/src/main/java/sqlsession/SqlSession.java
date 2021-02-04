package sqlsession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/11 9:28 上午
 */
public interface SqlSession {
    <E> List<E> selectAll(String statementId,Object... param) throws NoSuchFieldException, SQLException, InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException;
    <E> E selectOne(String statementId,Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException;
    <E> E getMapper(Class c);
 }
