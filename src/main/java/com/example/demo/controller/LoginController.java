package com.example.demo.controller;

import com.example.demo.JwtResponse;
import com.example.demo.JwtUtils;
import com.example.demo.MD5Encryptor;
import com.example.demo.mysql.DatabaseConnection;
import com.example.demo.pojo.User;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.demo.result.Result.jwtSuccess;

@Controller
public class LoginController {




    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) throws SQLException {
        //对html 标签进行转义，防止XSS攻击
        //分别与接收到的User类的username和password进行比较，根据结果的不停Result(不同的响应码)
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);


        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                String query = "SELECT id, username, password FROM users"; // 查询 AD 表
                PreparedStatement pstmt = conn.prepareStatement(query);
                // 执行查询并获取结果集
                ResultSet rs = pstmt.executeQuery();

                // 处理结果集
                while (rs.next()) {
                    String u = rs.getString("username");
                    String p = rs.getString("password");
                    String d = rs.getString("id");
                    System.out.println("用户");
                    if(Objects.equals(u,username) && Objects.equals(p, MD5Encryptor.encryptToMD5(requestUser.getPassword()))){

                        Map<String,Object> claims = new HashMap<>();
                        //将当前用户登录的业务数据封装传入
                        claims.put("id",d);
                        claims.put("username",username);
                        String token = JwtUtils.genToken(claims);
                        System.out.println(token);
                        //生成token令牌，其实是一段字符串
                        if(Objects.equals(d,"1")){

                            return Result.jwtSuccess1(token);
                        }else{

                        System.out.println("用户");
                        System.out.println(JwtUtils.parseToken(token));
                        return Result.jwtSuccess(token);
                        }
                    }


        }

     }
  }catch (Exception e){
            return new Result<>()
                    .code(400);
        }

return null;
    }
}
