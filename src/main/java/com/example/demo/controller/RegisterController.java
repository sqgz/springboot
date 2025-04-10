package com.example.demo.controller;

import com.example.demo.MD5Encryptor;
import com.example.demo.mysql.DatabaseConnection;
import com.example.demo.pojo.User;
import com.example.demo.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.sql.*;
import java.util.Objects;

@Controller
public class RegisterController {

    @CrossOrigin
    @PostMapping(value = "api/Register")
    @ResponseBody
    public Result register(@RequestBody User requestUser) throws SQLException {
        //对html 标签进行转义，防止XSS攻击
        //分别与接收到的User类的username和password进行比较，根据结果的不停Result(不同的响应码)
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

        Connection conn = null;

        try{
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                if (Objects.equals(requestUser.getConfirmPassword(),requestUser.getPassword()) && requestUser.getConfirmPassword()!=null) {
                    String query = "INSERT INTO users (username, password) VALUES (?,?)"; // 查询 AD 表
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1,username);
                    pstmt.setString(2, MD5Encryptor.encryptToMD5(requestUser.getPassword()));

                    pstmt.executeUpdate(); // 执行插入
                    System.out.println("成功!!");
                    return new Result<>()
                            .code(200);

                }else {
                    System.out.println("失败");
                    return new Result<>()
                            .code(400);
                }
            }
        }catch (SQLException ex) {
            System.out.println("失败");
            return new Result<>()
                    .code(400);
        }

       return null;
    }
}
