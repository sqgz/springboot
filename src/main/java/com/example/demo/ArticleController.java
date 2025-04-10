package com.example.demo;

import com.example.demo.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("login")
public class ArticleController {
    @GetMapping("/list")
    public Result list(@RequestHeader(name = "Authorization") String token, HttpServletResponse response){
        //验证token
        //并用提供的工具类解析和验证token
        try {
            Map<String, Object> claims = JwtUtils.parseToken(token);
            System.out.println("claims");

            return Result.success().code(200);

        } catch (Exception e) {
            //设置http响应状态码为400
            response.setStatus(401);
            return null;
        }
    }
}