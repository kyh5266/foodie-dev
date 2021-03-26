package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.CarouselMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_FACE = "https://git.imooc.com/class-73/Architect-Stage-1-Monolith/src/master/%e7%ac%ac1%e9%98%b6%e6%ae%b5%e5%8d%95%e4%bd%93%e6%9e%b6%e6%9e%84/imooc%20%e9%9d%99%e6%80%81%e8%b5%84%e6%ba%90/img/face2.png";

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

    /**
     * 创建用户
     *
     * @param userBO
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        Users users = new Users();
        users.setUsername(userBO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.setNickname(userBO.getUsername());
        users.setFace(USER_FACE);
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        users.setSex(Sex.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());
        return null;
    }
}
