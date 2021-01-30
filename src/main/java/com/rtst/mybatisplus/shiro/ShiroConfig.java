package com.rtst.mybatisplus.shiro;

import com.rtst.mybatisplus.filter.MyCrosFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author White Liu
 * @Description 详情
 * @Date 2020/9/21 11:46
 * @Version 1.0
 */
@Configuration
public class ShiroConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * 配置规则，过滤请求
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean sfb = new ShiroFilterFactoryBean();
        sfb.setSecurityManager(securityManager);//自定义的securityManager,不要导错了shiro自带的，不然自定义的realm类不起作用
        /*过滤器链定义映射
         * anon:所有url都都可以匿名访问，authc:所有url都必须认证通过才可以访问;
         * 过滤链定义，从上向下顺序执行，authc 应放在 anon 下面
         * */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("authc",new MyCrosFilter());
        sfb.setFilters(filterMap);
        // 未授权界面, 对应LoginController中 unauthorized 请求
        sfb.setUnauthorizedUrl("/api/unauthorized");
        //当shiro判断获取不到用户对象信息时，跳转到setLoginUrl方法参数中的路径，返回JSon数据给前端，让前端做登录跳转处理
        sfb.setLoginUrl("/api/un_auth");
        //swagger2资源免拦截
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger-resouces/**/**","anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        //登录、未认证、注册、登出请求免拦截
        filterChainDefinitionMap.put("/api/login", "anon");
        filterChainDefinitionMap.put("/api/un_auth", "anon");
        filterChainDefinitionMap.put("/api/user/register","anon");
        filterChainDefinitionMap.put("/api/**", "authc");

        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了, 位置放在 anon、authc下面
        filterChainDefinitionMap.put("/api/logout", "logout");


        sfb.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return sfb;
    }
    /**
     * 凭证匹配器（由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     *
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        //hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 将自己的验证方式加入容器
     *
     * @return MyShiroRealm
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm myShiroRealm = new ShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis, 使用的是shiro-redis开源插件
     * @return RedisSessionDAO
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        //设置用户缓存过期时间位5分钟,单位为秒将redis session的过期时间和springboot的session容器时间保持一致
        redisSessionDAO.setExpire(300);
        return redisSessionDAO;
    }

    /**
     * Session ID 生成器*
     * @return JavaUuidSessionIdGenerator
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 自定义sessionManager
     * @return SessionManager
     */
    @Bean
    public MySessionManager mySessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setGlobalSessionTimeout(300000);//设置session的过期时间，单位为毫秒，redisSessionDAO中的保持一致
        mySessionManager.setSessionDAO(redisSessionDAO());
        return mySessionManager;
    }

    /**
     * 配置shiro redisManager, 使用的是shiro-redis开源插件
     * @return RedisManager
     */

    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host+":"+port);//现在的setHost方法传参为IP:Port,默认值为127.0.0.1:6379
        redisManager.setTimeout( timeout);//默认值为2000
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现, 使用的是shiro-redis开源插件
     * @return RedisCacheManager
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // 必须要设置主键名称，shiro-redis 插件用这个缓存用户信息
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * description: 权限管理，配置主要是Realm的管理认证
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(mySessionManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }

    /**
     * 扫描上下文，寻找所有的Advistor(通知器）
     * 将这些Advisor应用到所有符合切入点的Bean中。
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }
    /**
     * 匹配所有加了 Shiro 认证注解的方法
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    @Bean
    public SimpleCookie cookie() {
        // cookie的name,对应的默认是 JSESSIONID
        SimpleCookie cookie = new SimpleCookie("SHARE_JSESSIONID");
        cookie.setHttpOnly(true);
        //  path为 / 用于多个系统共享 JSESSIONID
        cookie.setPath("/");
        return cookie;
    }
}
