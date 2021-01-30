package com.rtst.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rtst.mybatisplus.bean.CacheUser;
import com.rtst.mybatisplus.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
public interface UserService extends IService<User> {
    /**
     * 查询用户列表（分页）
     * @param pageInfo
     * @return
     */
    IPage<User> userList(Page<User> pageInfo);

    /**
     * 登录
     * @param userName
     * @param passWord
     * @return
     */
    CacheUser login(String userName, String passWord);

    /**
     * 退出登录
     */
    void logout();
}
