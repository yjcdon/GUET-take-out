package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * Author: 梁雨佳
     * Date: 2023/11/12 17:38:02
     * Description: 每分钟查询是否有超时的订单（超过15min不支付），超时的就把状态修改为已取消
     * 因为是直接去修改数据库，所以不需要发请求啥的
     * 修改谁？订单的状态是未付款并且创建时间到现在过了15min
     */
    @Scheduled(cron = "0 0/10 * * * ?")// 每10分钟执行一次
    // @Scheduled(cron = "0 * * * * ?")// 每分钟执行一次
    public void processTimeoutOrder () {
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (ordersList != null && !ordersList.isEmpty()) {
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            });
        }
    }

    /**
     * Author: 梁雨佳
     * Date: 2023/11/12 18:04:37
     * Description:每天凌晨1点，把上一天还在派送中状态的订单状态修改为已完成
     * 所以修改的数据时间是要减去一个小时，就是上一天的订单了-
     */
    @Scheduled(cron = "0 0 1 * * ?")// 每天凌晨1点执行一次
    public void processDeliveryOrder () {
        //log.info("开始处理未完成的订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (ordersList != null && !ordersList.isEmpty()) {
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            });
        }
    }
}
