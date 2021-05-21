package com.imooc.service;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Sid sid;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    /**
     * 创建订单
     *
     * @param submitOrderBO
     */
    @Override
    public void createOrder(SubmitOrderBO submitOrderBO) {
        Orders orders = new Orders();
        String orderId = sid.nextShort();

        // 1. 新订单数据保存
        orders.setId(orderId);
        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());
        orders.setIsComment(YesOrNo.NO.type);
        orders.setLeftMsg(submitOrderBO.getLeftMsg());
        orders.setIsDelete(YesOrNo.NO.type);
        orders.setPayMethod(submitOrderBO.getPayMethod());

        UserAddress userAddress = addressService.queryUserAddres(submitOrderBO.getUserId(),submitOrderBO.getAddressId());
        orders.setReceiverAddress(userAddress.getProvince() + "" + userAddress.getCity() + "" + userAddress.getDistrict() + ""
                                    + userAddress.getDetail());
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());
        orders.setUserId(submitOrderBO.getUserId());
        orders.setPostAmount(0);

        // 2. 循环根据itemSpecIds保存订单商品信息表
        String specIds[] =  submitOrderBO.getItemSpecIds().split(",");
        Integer totalAmount = 0;    // 商品原价累计
        Integer realPayAmount = 0;  // 优惠后的实际支付价格累计
        for(String specId: specIds) {

            // TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;

            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(specId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            // 2.3 循环保存子订单数据到数据库
            OrderItems orderItems = new OrderItems();
            orderItems.setId(sid.nextShort());
            orderItems.setBuyCounts(buyCounts);
            orderItems.setItemId(itemId);
            orderItems.setItemImg(imgUrl);
            orderItems.setItemName(item.getItemName());
            orderItems.setItemSpecId(specId);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItems.setOrderId(orderId);
            orderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(orderItems);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(specId, buyCounts);
        }
        orders.setRealPayAmount(realPayAmount);
        orders.setTotalAmount(totalAmount);
        ordersMapper.insert(orders);

        // 3. 保存订单状态表
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreatedTime(new Date());
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatusMapper.insert(orderStatus);

        // 4. 构建商户订单，用于传给支付中心

        // 5. 构建自定义订单vo
    }
}
