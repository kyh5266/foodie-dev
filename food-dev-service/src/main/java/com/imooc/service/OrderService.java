package com.imooc.service;

import com.imooc.pojo.bo.SubmitOrderBO;

public interface OrderService {

    /**
     * 创建订单
     * @param submitOrderBO
     */
    public String createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);
}
