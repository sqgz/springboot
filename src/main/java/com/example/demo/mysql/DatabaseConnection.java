package com.example.demo.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DatabaseConnection {
    // 私有化构造方法防止实例化
    private DatabaseConnection() {}

    // 数据库配置信息
    private static final String URL = "jdbc:mysql://localhost:3306/lib?useUnicode=true&characterEncoding=gbk";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    // 静态块加载数据库驱动（MySQL 8.0+）
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到MySQL JDBC驱动", e);
        }
    }

    /**
     * 获取数据库连接
     * @return Connection对象，连接失败返回null
     */
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("数据库连接成功！");
            return conn;
        } catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 关闭数据库连接
     * @param conn 需要关闭的连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接已关闭");
            } catch (SQLException e) {
                System.out.println("关闭连接时发生错误: " + e.getMessage());
            }
        }
    }
}
