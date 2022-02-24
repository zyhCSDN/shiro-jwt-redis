package com.maplexl.shiroJwtRedis.exception;

import com.maplexl.shiroJwtRedis.enums.StatusCode;
import com.maplexl.shiroJwtRedis.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * 全局异常
 *
 * @author:ZHAOYONGHENG
 * @date:2021/12/2
 * @version:1.0.0
 */

//@ControllerAdvice // 使用 @ControllerAdvice 实现全局异常处理
//@ResponseBody // @ResponseBody的作用其实是将java对象转为json格式的数据。
//@RestControllerAdvice作用等同于@ResponseBody加上@ControllerAdvice,会在所有带有@Controller或者@RestController注解的类上生效,还可以使用basePackages参数配置指定异常处理类生效的包
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 全局异常Exception捕获类
     * @param e
     * @return
     */

    @ExceptionHandler(value = Exception.class)
    public BaseResponse exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException)e;
            return new BaseResponse(ex.getStatusCode());
        } else if (e instanceof BindException){
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return new BaseResponse(StatusCode.SERVER_ERROR.getCode(),msg);
        } else {
            return new BaseResponse(StatusCode.SERVER_ERROR,e.getMessage());
        }
    }
}
