package com.rtst.mybatisplus.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rtst.mybatisplus.entity.SysPermission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
@Repository
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    Set<String> selectPermissionByRoleIds(List<Integer> roleIds);
}
