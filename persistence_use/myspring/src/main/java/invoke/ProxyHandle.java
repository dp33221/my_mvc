package invoke;

import java.lang.reflect.InvocationTargetException;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/28 10:32 下午
 */
public interface ProxyHandle {
    Object JdkDynamic(Object e);

    Object CgLibDynamic(Object e) throws InvocationTargetException, IllegalAccessException;
}
