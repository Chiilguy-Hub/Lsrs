package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用的API响应类，用于封装操作结果和响应数据。
 *
 * @param <T> 响应数据的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;       // 状态码，通常使用HTTP状态码
    private String message; // 响应消息或说明
    private T data;         // 响应数据

    /**
     * 自定义构造函数
     *
     * @param code    自定义返回枚举响应码，描述
     * @param message 自定义描述
     * @param data    返回数据
     */
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建一个成功的响应，不包含响应数据。
     *
     * @param <T> 响应数据的类型
     * @return 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "成功", null);
    }

    /**
     * 创建一个成功的响应，包含响应数据。
     *
     * @param data 响应数据
     * @param <T>  响应数据的类型
     * @return 成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success!", data);
    }

    /**
     * 创建一个失败的响应，包含状态码和消息，但不包含响应数据。
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     响应数据的类型
     * @return 失败响应
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 创建一个失败的响应，包含状态码、消息和响应数据。
     *
     * @param code    状态码
     * @param message 消息
     * @param data    响应数据
     * @param <T>     响应数据的类型
     * @return 失败响应
     */
    public static <T> Result<T> fail(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
 
 