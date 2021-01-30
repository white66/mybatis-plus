package com.rtst.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rtst.mybatisplus.entity.SysPermission;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
public interface SysPermissionService extends IService<SysPermission> {
    /**
     * 通过角色ID查询拥有的权限信息
     * @param roleIds
     * @return
     */
    Set<String> selectPermissionByRoleIds(List<Integer> roleIds);
}
