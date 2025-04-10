package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtTest {
    //生成JWT令牌的方法
    @Test
    public void testGen(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");
        //生成JWT代码
        String token = JWT.create()
                .withClaim("user",claims) //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12)) //添加过期时间
                .sign(Algorithm.HMAC256("feisi"));//指定算法，配置密钥
        //打印输出到控制台，查看生成的token令牌
        System.out.println(token);
    }
    //验证JWT令牌的方法
    @Test
    public void testParse(){
        //定义字符串，模拟用户传递给浏览器的token令牌
        //这个token就用上面生成的token令牌字符串就行
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                +".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3NDI5MDUxNjV9"
                +".tPRSkvfGRBhi8gRW2JhddXAFnQOkbsq1GeLmb4Y2AYE";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("feisi")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token); //验证token，生成一个解析后的JWT对象
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));

    }


}

