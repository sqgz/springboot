package com.example.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class MD5Encryptor {

    /**
     * 对字符串进行 MD5 加密
     *
     * @param input 要加密的字符串
     * @return MD5 加密后的字符串
     */
    public static String encryptToMD5(String input) {
        try {
            // 获取 MD5 消息摘要算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字符集将输入字符串转换为字节数组
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // 创建十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public static void main(String[] args) {
        String input = "your_string_to_encrypt"; // 替换为要加密的字符串
        String md5 = encryptToMD5(input);
        System.out.println("MD5 encrypted string: " + md5);
    }
}
