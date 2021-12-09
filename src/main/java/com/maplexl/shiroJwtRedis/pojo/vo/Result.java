package com.maplexl.shiroJwtRedis.pojo.vo;

import com.maplexl.shiroJwtRedis.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    T data;

    /**
     * 只有状态码和消息，无返回数据
     *
     * @param code 状态码
     * @param msg  消息
     */
    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 使用常用枚举设置状态码和消息
     *
     * @param commonEnum 常用枚举
     */
    public Result(CommonConstant commonEnum) {
        code = commonEnum.getCode();
        msg = commonEnum.getMsg();
    }

    /**
     * 使用常用枚举设置状态码和消息
     * 有数据
     * @param commonEnum 常用枚举
     * @param data 数据
     */
    public Result(CommonConstant commonEnum, T data) {
        code = commonEnum.getCode();
        msg = commonEnum.getMsg();
        this.data = data;
    }
}
