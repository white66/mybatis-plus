package com.rtst.mybatisplus.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rtst.mybatisplus.bean.BaseResult;
import com.rtst.mybatisplus.entity.User;
import com.rtst.mybatisplus.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @ApiOperation(value = "添加用户")
    @PostMapping("/register")
    public BaseResult addUser(@ApiParam(value = "用户对象",name = "User",required = true) @RequestBody User user){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name",user.getUserName());
        User userInfo = userService.getOne(userQueryWrapper);
        if(ObjectUtil.isNotNull(userInfo)){
            return BaseResult.ok("当前用户已存在！");
        }
        user.setPassWord(SecureUtil.md5(user.getPassWord()));//MD5加密
        boolean isSave = userService.save(user);
        if(!isSave){
            return BaseResult.ok("用户添加失败！");
        }
        return BaseResult.ok("添加用户成功！");
    }
    @ApiOperation(value = "查询用户列表(分页)")
    @GetMapping("/userList/{pageNum}/{pageSize}")
    @RequiresPermissions("user:get")
    public BaseResult userList(@PathVariable(value = "pageNum") int pageNum, @PathVariable(value = "pageSize") int pageSize){
        Page<User> pageInfo = new Page(pageNum,pageSize);
        IPage<User> users =  userService.userList(pageInfo);
        return BaseResult.ok().put("data",users);
    }
}

