package com.maplexl.shiroJwtRedis.constant;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
@SuppressWarnings("unused")
public enum CommonEnum implements CommonConstant {
    /**
     * 成功的操作
     */
    SUCCESS("成功！", CommonConstant.SUCCESS),
    /**
     * 请求错误
     */
    BAD_REQUEST("请求错误", CommonConstant.BAD_REQUEST),
    /**
     * 非法的参数
     */
    INVALID_CHARACTER("非法的参数", CommonConstant.INVALID_CHARACTER),
    /**
     * 资源未找到
     */
    NOTFOUND("资源未找到", CommonConstant.NOTFOUND),
    /**
     * 请求方式错误
     */
    ERROR_METHOD("请求方式错误", CommonConstant.ERROR_METHOD),
    /**
     * 没有令牌
     */
    NO_TOKEN("没有令牌", CommonConstant.NO_TOKEN),
    /**
     * 令牌过期
     */
    TOKEN_EXPIRED("令牌过期", CommonConstant.TOKEN_EXPIRED),
    /**
     * 错误的令牌
     */
    TOKEN_ERROR("错误的令牌", CommonConstant.TOKEN_ERROR),
    /**
     * 插入失败
     */
    INVALID_INSERT("插入失败", CommonConstant.INVALID_INSERT),
    /**
     * 查询失败
     */
    INVALID_SELECT("查询失败", CommonConstant.INVALID_SELECT),
    /**
     * 删除失败
     */
    INVALID_DELETE("删除失败", CommonConstant.INVALID_DELETE),
    /**
     * 更新失败
     */
    INVALID_UPDATE("更新失败", CommonConstant.INVALID_UPDATE),
    /**
     * 不支持的媒体格式
     */
    ERROR_FORMAT("不支持的媒体格式", CommonConstant.ERROR_FORMAT),
    /**
     * 用户已被注册
     */
    USER_EXIST("用户已被注册", CommonConstant.USER_EXIST),
    /**
     * 用户不存在
     */
    NO_SUCH_USER("用户不存在", CommonConstant.NO_SUCH_USER),
    /**
     * 密码错误
     */
    PASSWORD_ERROR("密码错误", CommonConstant.PASSWORD_ERROR),
    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION("未知异常", CommonConstant.UNKNOWN_EXCEPTION),
    ;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    CommonEnum(String msg, int code) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 状态码
     */
    private final int code;
    /**
     * 消息
     */
    private final String msg;
}
