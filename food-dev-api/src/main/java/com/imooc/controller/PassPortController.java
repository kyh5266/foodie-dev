package com.imooc.controller;

import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.IMOOCJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("passport")
public class PassPortController {

    @Autowired
    private UserService userService;

    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {
        //1.判断用户名是否为空
        if(StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }

        //2.查找用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        //3.请求成功
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPass = userBO.getConfirmPassword();

        //0.非空判断
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPass)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        //1.查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        //2.密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能小于6位");
        }

        //3.判断两次密码是否一致
        if (!password.equals(confirmPass)) {
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }

        //4.进行注册
        userService.createUser(userBO);

        return IMOOCJSONResult.ok();

    }

}
