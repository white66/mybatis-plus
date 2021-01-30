package com.rtst.mybatisplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @Author White Liu
 * @Description Swagger配置类
 * @Date 2020/9/18 21:56
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(Environment environment){
        //设置要启动swagger2的项目环境
        Profiles profiles = Profiles.of("dev","test");
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .groupName("XXXAPI文档")
                    .enable(flag)//是否启动swagger2
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.rtst.mybatisplus"))
                    .build();
    }
    private ApiInfo apiInfo(){
        return new ApiInfo("Api 文档",
                     "Api Documentation",
                        "1.0",
                "urn:tos",
                                new Contact("White Liu", "121", "131as"),
                        "Apache 2.0",
                      "http://www.apache.org/licenses/LICENSE-2.0",
                                new ArrayList());
    }
}
