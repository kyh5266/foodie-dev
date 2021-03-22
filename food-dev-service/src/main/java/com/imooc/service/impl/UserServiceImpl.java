package com.imooc.service.impl;

import com.imooc.mapper.CarouselMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UsersMapper usersMapper;

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example example = new Example(Users.class);
        example.createCriteria().andEqualTo("username",username);
        Users res =  usersMapper.selectOneByExample(example);
        return res == null ? false: true;
    }
}
