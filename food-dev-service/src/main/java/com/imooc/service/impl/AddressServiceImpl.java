package com.imooc.service.impl;

import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 根据用户ID查询用户的收货地址列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        return userAddressMapper.select(userAddress);
    }

    /**
     * 用户新增收货地址
     *
     * @param addressBO
     */
    @Override
    public void addNewUserAddress(AddressBO addressBO) {

        //1.判断当前用户是否存在地址，没有则新增为默认地址
        Integer isDefault = 0;
        List<UserAddress> list = this.queryAll(addressBO.getUserId());
        if(list.size()==0 || list == null || list.isEmpty()) {
            isDefault = 1;
        }

        //2.保存到数据库
    }

}
