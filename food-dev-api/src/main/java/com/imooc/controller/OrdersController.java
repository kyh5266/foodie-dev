package com.imooc.controller;

import com.imooc.enums.PayMethod;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.service.OrderService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单" ,httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {
        System.out.println(submitOrderBO.toString());

        if (submitOrderBO.getPayMethod()!=PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("不支持此支付方式");
        }

        //1.创建订单
        orderService.createOrder(submitOrderBO);
        //2.创建订单以后移除购物车中的已结算（已提交）的商品
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据

        return IMOOCJSONResult.ok();
    }
}
