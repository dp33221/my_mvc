package config;

import lombok.Builder;
import lombok.Data;

/**
 * @author dingpei
 * @Description: mapper转换类
 * @date 2020/11/10 6:41 下午
 */
@Data
@Builder
public class MappedStatement {
    private String id;
    private Class<?> paramType;
    private Class<?> resultType;
    private String sql;
    private String executorType;
}
