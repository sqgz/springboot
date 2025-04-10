package com.example.demo.controller;


import com.example.demo.mysql.DatabaseConnection;
import com.example.demo.pojo.*;
import com.example.demo.result.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class UserController {



    @GetMapping(value = "api/User/{id}")
    @CrossOrigin(origins = "http://localhost:8080") // 允许Vue前端访问
    public Result<User> getUser(@PathVariable int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM users WHERE id = ?"; // 假设表名为user
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();



            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                // 设置其他字段...

                // 返回成功结果
                return new Result<User>()
                        .code(200)
                        .data(user);
            } else {
                // 用户不存在
                return new Result<User>()
                        .code(404)
                        .message("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Result<User>()
                    .code(500)
                    .message("Database error: " + e.getMessage());
        } finally {
            // 关闭资源
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @ResponseBody
    @GetMapping(value = "api/BorrowedBooks/{id}")
    @CrossOrigin(origins = "http://localhost:8080") // 允许Vue前端访问
    public BORROWRE getBorrowRecords(@PathVariable int id) {
        Connection conn2 = null;
        List<BookTitle> titleList = new ArrayList<>();
        try{
        conn2 = DatabaseConnection.getConnection();
        PreparedStatement pstmt2 = null;
        ResultSet rs2 = null;
        String query2 = "SELECT b.title AS book_title\n" +
                "FROM book_user bu\n" +
                "JOIN book b ON bu.book_id = b.id\n" +
                "WHERE bu.user_id = ?";
        pstmt2 = conn2.prepareStatement(query2);
        pstmt2.setInt(1, id);
        rs2 = pstmt2.executeQuery();
        while (rs2.next()) {
            System.out.println(rs2.getString("book_title"));
            BookTitle book_title = new BookTitle(
                    rs2.getString("book_title")
            );

            titleList.add(book_title);
        }
        return new BORROWRE(titleList);
    } catch (SQLException e){
            e.printStackTrace();
            return new BORROWRE(Collections.emptyList());

        }

    }

}
