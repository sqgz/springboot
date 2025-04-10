package com.example.demo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorMapping_tset1 implements WebMvcConfigurer {

    @Autowired
    private Interceptor_test1 loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并指定拦截路径
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/static/**", "/css/**", "/js/**","/api/login/**","/api/register/**"); // 排除静态资源
    }
}

