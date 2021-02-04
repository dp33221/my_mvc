package annotation;

import java.lang.annotation.*;

/**
 * @author dingpei
 * @Description: 业务层注入
 * @date 2020/11/23 10:21 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyService {
    String value() default "";
}
