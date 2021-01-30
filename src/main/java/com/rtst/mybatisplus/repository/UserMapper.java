package com.rtst.mybatisplus.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rtst.mybatisplus.entity.SysUserRole;
import com.rtst.mybatisplus.entity.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectPageVo(Page<User> pageInfo, Object o);

    /**
     * 通过用户名获取所拥有的角色ID
     * @param userName
     * @return
     */
    SysUserRole findRoleByUserName(String userName);
}
