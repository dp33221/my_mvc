package config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/10 6:39 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    private DataSource dataSource;
    @Builder.Default
    private Map<String,MappedStatement> mappedStatementList=new HashMap<String, MappedStatement>();

}
