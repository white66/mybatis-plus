package com.rtst.mybatisplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author White Liu
 * @Description 详情
 * @Date 2020/9/23 16:37
 * @Version 1.0
 */
@Configuration
public class CrosConfig {
    @Bean
    public WebMvcConfigurer CORSConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        //设置是否允许跨域传cookie
                        .allowCredentials(true)
                        //设置缓存时间，减少重复响应
                        .maxAge(3600);
            }
        };
    }
}
