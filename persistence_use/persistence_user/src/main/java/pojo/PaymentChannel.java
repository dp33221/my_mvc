package pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/10 2:10 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentChannel {
    private Integer id;
    /**
     * 渠道名
     */
    private String channel_name;
    /**
     * 渠道code
     */
    private String channel_code;
    /**
     * 渠道
     */
    private String gate_id;
    /**
     * 渠道状态
     */
    private Integer channel_status;

    private Date create_time;

    private Date update_time;
}
