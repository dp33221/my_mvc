package annotation;

import java.lang.annotation.*;

/**
 * @author dingpei
 * @Description: 事务
 * @date 2020/11/23 10:21 下午
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyTransactional {
}
