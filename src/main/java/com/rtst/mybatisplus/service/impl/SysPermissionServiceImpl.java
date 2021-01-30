package com.rtst.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rtst.mybatisplus.entity.SysPermission;
import com.rtst.mybatisplus.repository.SysPermissionMapper;
import com.rtst.mybatisplus.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Autowired
    SysPermissionMapper sysPermissionMapper;
    @Override
    public Set<String> selectPermissionByRoleIds(List<Integer> roleIds) {
        Set<String> permissions =sysPermissionMapper.selectPermissionByRoleIds(roleIds);
        return permissions;
    }
}
