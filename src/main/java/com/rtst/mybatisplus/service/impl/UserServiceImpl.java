package com.rtst.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rtst.mybatisplus.bean.CacheUser;
import com.rtst.mybatisplus.entity.SysUserRole;
import com.rtst.mybatisplus.entity.User;
import com.rtst.mybatisplus.exception.LoginException;
import com.rtst.mybatisplus.repository.UserMapper;
import com.rtst.mybatisplus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public IPage<User> userList(Page<User> pageInfo) {
        IPage<User> users =  userMapper.selectPageVo(pageInfo,null);
        return users;
    }

    @Override
    public CacheUser login(String userName, String passWord) {
        //获取shiro的Subject实例对象，获取用户信息
        Subject currentUser = SecurityUtils.getSubject();
        //将用户名和密码封装到UsernamePasswordToken中
        UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord);
        CacheUser cacheUser;
        // 4、认证
        try {
            // 传到 MyShiroRealm 类中的方法进行认证
            currentUser.login(token);
            // 构建缓存用户信息返回给前端
            User user = (User) currentUser.getPrincipals().getPrimaryPrincipal();
            System.out.println("------获取session时间："+currentUser.getSession().getTimeout());
            cacheUser = CacheUser.builder()
                    .token(currentUser.getSession().getId().toString())
                    .build();
            BeanUtils.copyProperties(user, cacheUser);
            SysUserRole userRole = userMapper.findRoleByUserName(userName);
            cacheUser.setRoleId(userRole.getRoleId());
            log.warn("CacheUser is {}", cacheUser.toString());
        } catch (UnknownAccountException e) {
            log.error("账户不存在异常：", e);
            throw new LoginException("账号不存在!", e);
        } catch (IncorrectCredentialsException e) {
            log.error("凭据错误（密码错误）异常：", e);
            throw new LoginException("密码不正确!", e);
        } catch (AuthenticationException e) {
            log.error("身份验证异常:", e);
            throw new LoginException("用户验证失败!", e);
        }
        return cacheUser;
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
