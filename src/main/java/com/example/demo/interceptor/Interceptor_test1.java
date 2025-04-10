package com.example.demo.interceptor;


import com.example.demo.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
public class Interceptor_test1 implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");

        //解析token
        //用提供的工具类解析和验证token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("一个options");
            return true; // 放行 OPTIONS 请求
        }
        try {
            String tokenPart = token.substring(7);
            Map<String, Object> claims = JwtUtils.parseToken(tokenPart);
            //放行
            System.out.println("fanxin");
            return true;
        } catch (Exception e) {
            //设置http响应状态码为401
            response.setStatus(401);
            System.out.println("bufanxin");
            //不放行
            return false;
        }



    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        System.out.println("LoggingInterceptor - postHandle: 请求处理完成");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        System.out.println("LoggingInterceptor - afterCompletion: 请求结束");

    }
}

