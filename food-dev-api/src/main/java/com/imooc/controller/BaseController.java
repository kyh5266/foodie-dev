package com.imooc.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART="shopcart";

    public static final Integer COMMON_PAGE_SIZE=10;
    public static final Integer PAGE_SIZE=20;

    // 支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    //回调通知的URL
    String payReturnUrl = "http://xunyu.natapp4.cc:/orders/notifyMerchantOrderPaid";

}
