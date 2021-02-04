package mapper;

import pojo.PaymentChannel;

import java.util.List;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/12 9:35 下午
 */
public interface PaymentChannelMapper {
    List<PaymentChannel> selectAll();
    PaymentChannel selectOne(PaymentChannel paymentChannel);
    int update(Integer id,String channel_name);
    int insert(PaymentChannel paymentChannel);
    int delete(int id);
}
