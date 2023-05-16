package com.restkeeper.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created With IntelliJ IDEA
 * User: yao
 * Date:2023/04/24
 * Description: 统一异常拦截
 * Version:V1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public Object Exception(Exception ex) {
        ExceptionResponse response =new ExceptionResponse(ex.getMessage());
        return response;
    }
}
