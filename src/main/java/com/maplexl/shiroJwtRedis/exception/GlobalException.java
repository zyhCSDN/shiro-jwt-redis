package com.maplexl.shiroJwtRedis.exception;


import com.maplexl.shiroJwtRedis.enums.StatusCode;

/**
 *
 * @author:ZHAOYONGHENG
 * @date:2021/12/2
 * @version:1.0.0
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = -7034897193246939L;

    private StatusCode statusCode;

    public GlobalException(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
