package com.example.demo.controller;

import com.example.demo.mysql.DatabaseConnection;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.BookResponse;
import com.example.demo.pojo.PageableInfo;
import org.hibernate.Remove;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:8080") // 允许Vue前端访问
public class BookController {


    @GetMapping
    public BookResponse getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> bookList = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();

            // 查询总记录数
            String countQuery = "SELECT COUNT(*) FROM book";
            if (search != null && !search.isEmpty()) {
                countQuery += " WHERE title LIKE ? OR author LIKE ?";
            }
            pstmt = conn.prepareStatement(countQuery);
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
            }
            rs = pstmt.executeQuery();
            rs.next();
            int totalItems = rs.getInt(1);

            // 查询分页数据
            String query = "SELECT * FROM book";
            if (search != null && !search.isEmpty()) {
                query += " WHERE title LIKE ? OR author LIKE ?";
            }
            query += " LIMIT ? OFFSET ?";
            pstmt = conn.prepareStatement(query);
            int index = 1;
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                pstmt.setString(index++, searchPattern);
                pstmt.setString(index++, searchPattern);
            }
            pstmt.setInt(index++, size);
            pstmt.setInt(index, page * size);
            rs = pstmt.executeQuery();

            // 将结果集转换为Book对象列表
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("status"),
                        rs.getString("created_at")
                );
                bookList.add(book);
            }

            // 计算分页信息
            int totalPages = (int) Math.ceil((double) totalItems / size);

            // 返回分页数据和分页信息
            return new BookResponse(bookList, new PageableInfo(page, size, totalItems, totalPages));

        } catch (Exception e) {
            e.printStackTrace();
            return new BookResponse(Collections.emptyList(), new PageableInfo(0, 0, 0, 0));
        } finally {
            // 关闭数据库资源
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @PutMapping("/{id}/{status}/{uid}")
    public ResponseEntity<?> updateBorrowStatus(
            @PathVariable int id,
            @PathVariable int status,
            @PathVariable int uid) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;
        try {
            System.out.println(status);
            if(status == 1) {
                status=0;
            }else{
                status=1;
            }
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE book SET status = ? WHERE id = ?";
            String sql2 = "INSERT INTO book_user (book_id, user_id) VALUES (?, ?)";
            String sql3 = "DELETE FROM book_user WHERE book_id = ?";
            String sql4 = "SELECT COUNT(*) AS borrowed\n" +
                    "FROM book_user\n" +
                    "WHERE user_id = ? AND book_id = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, id);
            pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, id);
            pstmt2.setInt(2, uid);
            pstmt3 = conn.prepareStatement(sql3);
            pstmt3.setInt(1, id);
            pstmt4 = conn.prepareStatement(sql4);
            pstmt4.setInt(1, uid);
            pstmt4.setInt(2, id);
            ResultSet rs = pstmt4.executeQuery();

            if(status == 0) {

                pstmt2.executeUpdate(); // 执行插入
                System.out.println("借书");
            }else{


               if (rs.next()) { // 检查是否有数据
                    int n = rs.getInt("borrowed");
                if(n==1){
                    pstmt3.executeUpdate();
                    System.out.println("还书");
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap("message", "Book not found"));
                }

                }
            }
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Book not found"));
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Update failed"));
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletebook(@PathVariable int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 获取数据库连接
            conn = DatabaseConnection.getConnection();

            // 开启事务
            conn.setAutoCommit(false);

            // 删除指定的记录
            String deleteSql = "DELETE FROM book WHERE id = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            // 禁用外键检查
            String disableForeignKeyCheckSql = "SET FOREIGN_KEY_CHECKS = 0";
            pstmt = conn.prepareStatement(disableForeignKeyCheckSql);
            pstmt.executeUpdate();

            // 重置 AUTO_INCREMENT
            String resetAutoIncrementSql = "ALTER TABLE book AUTO_INCREMENT = 1";
            pstmt = conn.prepareStatement(resetAutoIncrementSql);
            pstmt.executeUpdate();

            // 更新 id 值
            String updateIdSql = "SET @count = 0";
            pstmt = conn.prepareStatement(updateIdSql);
            pstmt.executeUpdate();

            updateIdSql = "UPDATE book SET id = @count:= @count + 1";
            pstmt = conn.prepareStatement(updateIdSql);
            pstmt.executeUpdate();

            // 重新启用外键检查
            String enableForeignKeyCheckSql = "SET FOREIGN_KEY_CHECKS = 1";
            pstmt = conn.prepareStatement(enableForeignKeyCheckSql);
            pstmt.executeUpdate();

            // 提交事务
            conn.commit();

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // 回滚事务
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // 恢复自动提交
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.internalServerError().build();
    }





}