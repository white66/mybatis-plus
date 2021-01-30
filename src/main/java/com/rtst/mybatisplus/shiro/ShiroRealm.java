package com.rtst.mybatisplus.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rtst.mybatisplus.entity.SysRole;
import com.rtst.mybatisplus.entity.SysUserRole;
import com.rtst.mybatisplus.entity.User;
import com.rtst.mybatisplus.repository.UserMapper;
import com.rtst.mybatisplus.service.impl.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义 shiroRealm, 主要是重写其认证、授权
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    SysUserRoleServiceImpl sysUserRoleService;
    @Autowired
    SysRoleServiceImpl sysRoleService;
    @Autowired
    SysPermissionServiceImpl sysPermissionService;
    @Autowired
    SysRolePermissionServiceImpl rolePermissionService;
    @Autowired
    UserMapper userMapper;
    /**
     * 权限配置，注入权限
     * @param principalCollection
     * @return 权限信息，包括角色以及权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();

        QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
        sysUserRoleQueryWrapper.eq("userId",user.getId());
        //根据用户Id查询角色信息,一个用户可能拥有多个角色
        List<SysRole> roleList = sysRoleService.listByIds(sysUserRoleService.list(sysUserRoleQueryWrapper).stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));
        List<Integer> roleIds = roleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        Set<String> roleSet = roleList.stream().map(SysRole::getRoleName).collect(Collectors.toSet());

        // 放入角色信息
        simpleAuthorizationInfo.setRoles(roleSet);
        //放入权限信息
        System.out.println("放入权限");
        Set<String> sysPermissions = sysPermissionService.selectPermissionByRoleIds(roleIds);
        sysPermissions.forEach(System.out::println);
        simpleAuthorizationInfo.setStringPermissions(sysPermissions);
        return simpleAuthorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     * @param authenticationToken
     * @return 身份验证信息
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //根据用户名查询用户对象
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name",userToken.getUsername());
        User user = userService.getOne(userQueryWrapper);
        //账号不存在
        if(null == user) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        return new SimpleAuthenticationInfo(user, user.getPassWord(), getName());
    }
}
