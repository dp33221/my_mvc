package service;

import annotation.MyAutowired;
import annotation.MyService;
import annotation.MyTransactional;
import mapper.PaymentChannelMapper;
import pojo.PaymentChannel;

import java.util.List;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/30 10:41 下午
 */
@MyService
public class MySimpleSpringServiceJdk implements IMySimpleSpringService {
    @MyAutowired
    private PaymentChannelMapper paymentChannelMapper;

    @Override
    @MyTransactional
    public List<PaymentChannel> test() {
//        final PaymentChannelMapper paymentChannelMapper = new DefaultBeanFactory().getBean("paymentChannelMapper");
//        final List<PaymentChannel> paymentChannels = paymentChannelMapper.selectAll();
//        for (PaymentChannel paymentChannel : paymentChannels) {
//            System.out.println(paymentChannel);
//        }
        return paymentChannelMapper.selectAll();
//        int i=1/0;
//        paymentChannelMapper.selectAll();


    }
}
