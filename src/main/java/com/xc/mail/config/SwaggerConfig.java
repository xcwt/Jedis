package com.xc.mail.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * Swaagger 配置
 */

@Configuration
public class SwaggerConfig {

    /**
     * 配置swagger2
     */
    @Bean
    public Docket docket(Environment environment){

        //获取项目环境：如果是dev或者是test返回true ,这个一般用于配置环境和发布环境的切换
        //Profiles profiles = Profiles.of("dev","test");
        //boolean flag = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //是否开启swagger
                //.enable(flag)
                .select()
                //RequestHandlerSelectors配置要扫描接口的方式 1、basePackage：基于包扫描 2、withMethodAnnotation 扫描方法上的注解
//                .apis(RequestHandlerSelectors.basePackage("com.alan.swaggerdemo.controller"))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(GetMapping.class))
                //paths(),过滤什么路径，只扫描什么路径下的文件
                //.paths(PathSelectors.ant("/alan/xxx"))
                .build();//build
    }

    /**
     * 配置Swagger信息 apiInfo
     */
    public ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("Alan ture", "www.baidu.com", "xxxxxx.qq.com");
        return new ApiInfo(
                "Alan ture SwaggerAPi文档",
                "这个是描述",
                "1.0",
                "www.baidu.com",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>());
    }

}
