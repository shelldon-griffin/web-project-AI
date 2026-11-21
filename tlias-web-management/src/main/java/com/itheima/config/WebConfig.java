package com.itheima.config;

import com.itheima.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //自定义的拦截器对象
    // 过滤器filter和拦截器interceptor同时使用，前端请求先经过过滤器，然后再经过拦截器(响应时相反)
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器对象
        // /depts/**：可以匹配 /depts下的任意级路径。/depts/*：可以匹配 /depts下的一级路径。
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")//设置拦截器拦截的请求路径（ /** 表示拦截所有请求）
                .excludePathPatterns("/login");//设置不拦截的请求路径
    }
}
