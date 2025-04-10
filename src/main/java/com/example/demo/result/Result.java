package com.example.demo.result;

import com.example.demo.pojo.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;    // 状态码
    private String msg;  // 提示信息
    private T data;      // 业务数据

    // 成功静态方法（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 成功静态方法（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败静态方法（通用错误）
    public static <T> Result<T> fail(String msg) {
        return new Result<>(500, msg, null);
    }

    // 失败静态方法（自定义状态码）
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // 新增方法：返回包含JWT的响应
    public static Result<String> jwtSuccess(String jwt) {
        return new Result<>(200, "JWT获取成功", jwt);
    }
    public static Result<String> jwtSuccess1(String jwt) {
        return new Result<>(300, "JWT获取成功", jwt);
    }
    // 链式调用方法（可选）
    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public Result<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> message(String userNotFound) {
        return fail(400, userNotFound);
    }


}