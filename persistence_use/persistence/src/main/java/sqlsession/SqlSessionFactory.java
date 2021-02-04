package sqlsession;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/10 6:48 下午
 */
public interface SqlSessionFactory {
    DefaultSqlSession openSession();
}
