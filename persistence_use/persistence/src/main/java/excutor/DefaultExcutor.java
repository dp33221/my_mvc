package excutor;

import config.Configuration;
import config.MappedStatement;
import resources.Resource;
import utils.GenericTokenParser;
import utils.ParameterMapping;
import utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dingpei
 * @Description: 默认执行器
 * @date 2020/11/11 9:34 下午
 */
public class DefaultExcutor implements Excutor {

    /**
     * @Description: select执行
     * @param configuration 配置解析
     * @param mappedStatement mapper解析
     * @param params 入参
     * @return java.util.List<E>
     * @Author: dingpei
     * @Date: 2020/11/11 9:38 下午
     */
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        //获取链接
        Connection connection = Resource.conn.get();
        if(connection==null){
            connection=configuration.getDataSource().getConnection();
            Resource.conn.set(connection);
        }
        //获取sql
        final String sql = mappedStatement.getSql();
        //新建处理器
        final ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //解析sql 将#{}转化为？
        final String parse = new GenericTokenParser("#{", "}", parameterMappingTokenHandler).parse(sql);
        //sql中#{}包含的集合
        final List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        //预编译sql
        PreparedStatement preparedStatement=connection.prepareStatement(parse);
        //找出入参类型
        final Class<?> paramType = mappedStatement.getParamType();
        if(paramType!=null){
            int index=1;
            for (ParameterMapping parameterMapping : parameterMappings) {
                //字段名
                final String name = parameterMapping.getName();
                //反射获取字段
                final Field declaredField = paramType.getDeclaredField(name);
                //开启访问权限
                declaredField.setAccessible(true);
                //获取入参的值
                final Object o = declaredField.get(params[0]);
                preparedStatement.setObject(index,o);
                index++;
            }
        }

        final ResultSet resultSet = preparedStatement.executeQuery();
        //获取返回值类型
        final Class<?> resultType = mappedStatement.getResultType();
        List<E> res=new ArrayList<>();
        //封装返回结果集
        while(resultSet.next()){
            //查询结果
            final ResultSetMetaData metaData = resultSet.getMetaData();
            //获取多少列数
            final int columnCount = metaData.getColumnCount();
            final Object o = resultType.newInstance();
            //遍历结果并封装对象
            for(int i=1;i<=columnCount;i++){
                //字段名
                final String columnName = metaData.getColumnName(i);
                //字段值
                final Object value = resultSet.getObject(columnName);
                //内省
                PropertyDescriptor propertyDescriptor=new PropertyDescriptor(columnName,resultType);
                //创建写方法
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);
            }
            res.add((E) o);

        }

        return res;
    }

    /**
     * @Description: 增删改操作
     * @param configuration 配置信息
     * @param mappedStatement xml解析信息
     * @param params 入参
     * @return int
     * @Author: dingpei
     * @Date: 2020/11/17 8:39 下午
     */
    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Parameter[] parameters, Object... params) throws SQLException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        //获取链接
        Connection connection = Resource.conn.get();
        if(connection==null){
            connection=configuration.getDataSource().getConnection();
            Resource.conn.set(connection);
        }
        //获取sql
        final String sql = mappedStatement.getSql();
        //新建处理器
        final ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //解析sql 将#{}转化为？
        final String parse = new GenericTokenParser("#{", "}", parameterMappingTokenHandler).parse(sql);
        //sql中#{}包含的集合
        final List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        //预编译sql
        PreparedStatement preparedStatement=connection.prepareStatement(parse);
        //获取入参类型
        final Class<?> paramType = mappedStatement.getParamType();
        //如果未设置入参类型，则默认基本数据类型或引用数据类型
        if(paramType==null){
            //将传入的值排序
            for (int j=0;j<parameterMappings.size();j++) {
                //parameterMappings是参数插入的顺序，在型参找到对应的位置
                for (int i = 0; i < parameters.length; i++) {
                    if(parameterMappings.get(j).getName().equals(parameters[i].getName())){
                        preparedStatement.setObject(j+1,params[i]);
                    }
                }

            }

        }
        //
        if(paramType!=null){
            int index=1;
            for (ParameterMapping parameterMapping : parameterMappings) {
                //字段名
                final String name = parameterMapping.getName();
                //反射获取字段
                final Field declaredField = paramType.getDeclaredField(name);
                //开启访问权限
                declaredField.setAccessible(true);
                //获取入参的值
                final Object o = declaredField.get(params[0]);
                preparedStatement.setObject(index,o);
                index++;
            }
        }
        return preparedStatement.executeUpdate();

    }

}
