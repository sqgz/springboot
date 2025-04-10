const express = require('express');
const mysql = require('mysql2/promise'); // 改用 promise 版本
const cors = require('cors');
const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

// 创建数据库连接池（推荐）
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '123456',
    database: 'example_db',
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0,
    timezone: '+08:00',
    charset: 'utf8mb4'// 设置时区为东八区
});

// 数据库初始化检查（可选）
async function initializeDatabase() {
    try {
        await pool.query(`
            CREATE TABLE IF NOT EXISTS messages (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL,
                content TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
        `);
        console.log('确保 messages 表存在');
    } catch (err) {
        console.error('数据库初始化失败:', err);
    }
}
initializeDatabase();

// 获取所有留言（带时间戳）
app.get('/messages', async (req, res) => {
    try {
        const [results] = await pool.query(`
            SELECT 
                id,
                username,
                content,
                DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') AS created_at
            FROM messages
            ORDER BY created_at DESC
        `);
        res.json(results);
    } catch (err) {
        console.error('获取留言失败:', err);
        res.status(500).json({ error: '获取留言失败' });
    }
});

// 添加新留言
app.post('/messages', async (req, res) => {
    const { username, content } = req.body;

    // 输入验证
    if (!username || !content) {
        return res.status(400).json({ error: '用户名和内容不能为空' });
    }

    try {
        const [result] = await pool.query(
            'INSERT INTO messages (username, content) VALUES (?, ?)',
            [username.trim(), content.trim()]
        );

        // 返回新创建的留言（包含完整数据）
        const [newMessage] = await pool.query(`
            SELECT 
                id,
                username,
                content,
                DATE_FORMAT(created_at, '%Y-%m-%d %H:%i:%s') AS created_at
            FROM messages
            WHERE id = ?`,
            [result.insertId]
        );

        res.status(201).json(newMessage[0]);
    } catch (err) {
        console.error('添加留言失败:', err);
        res.status(500).json({ error: '添加留言失败' });
    }
});

app.listen(port, () => {
    console.log(`服务器运行在 http://localhost:${port}`);
});