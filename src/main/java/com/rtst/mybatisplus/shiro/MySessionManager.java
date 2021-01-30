package com.rtst.mybatisplus.shiro;

import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @Author White Liu
 * @Description 详情
 * @Date 2020/9/21 14:17
 * @Version 1.0
 */
public class MySessionManager extends DefaultWebSessionManager {
    private static final String AUTHORIZATION = "Authorization";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public MySessionManager() {
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //从前端请求 headers中获取这个Authorization参数用来判断授权
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        if (StringUtils.hasLength(id)) {//
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            System.out.println("获取authorization字段"+id);
            return id;
        } else {
            //从前端的cookie中取值
            System.out.println("从前端获取authorization字段");
            return super.getSessionId(request, response);
        }

    }
}
