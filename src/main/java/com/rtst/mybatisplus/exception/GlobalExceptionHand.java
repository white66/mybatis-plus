package com.rtst.mybatisplus.exception;


import com.rtst.mybatisplus.bean.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.management.ServiceNotFoundException;
import java.util.List;

/**
 * 全局异常处理
 */
@EnableWebMvc
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHand {
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String msg = "缺少请求参数！";
        log.error(msg, e);
        return BaseResult.error(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String msg = e.getMessage();
        log.error("参数解析失败：", e);
        return BaseResult.error(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = handleBindingResult(e.getBindingResult());
        log.error("方法参数无效: ", e);
        return BaseResult.error(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public BaseResult handleBindException(BindException e) {
        String msg = handleBindingResult(e.getBindingResult());
        log.error("参数绑定失败:", e);
        return BaseResult.error(msg);
    }
    /**
     * 401 - Unauthorized
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException.class)
    public BaseResult handleLoginException(LoginException e) {
        String msg = e.getMessage();
        log.error("登录异常：", e);
        return BaseResult.error(msg);
    }
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    @ExceptionHandler(AuthorizationException.class)
    public BaseResult authorizationException(AuthorizationException e){
        log.error("未知身份异常操作");
        return BaseResult.error(400,"未知身份异常操作");
    }
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    @ExceptionHandler(UnavailableSecurityManagerException.class)
    public BaseResult unavailableSecurityManagerException(UnavailableSecurityManagerException e){
        log.error("用户未登录",e);
        return BaseResult.error(500,"用户未登录");
    }
    /**
     * 403 - Unauthorized
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public BaseResult handleLoginException(UnauthorizedException e) {
        log.error("用户无权限：", e);
        return BaseResult.error(403, "用户无权限.....");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String msg = "不支持当前请求方法！";
        log.error(msg, e);
        return BaseResult.error(msg);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BaseResult handleHttpMediaTypeNotSupportedException(Exception e) {
        String msg = "不支持当前媒体类型！";
        log.error(msg, e);
        return BaseResult.error(msg);
    }

    /**
     * 422 - UNPROCESSABLE_ENTITY
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResult handleMaxUploadSizeExceededException(Exception e) {
        String msg = "所上传文件大小超过最大限制，上传失败！";
        log.error(msg, e);
        return BaseResult.error(msg);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceNotFoundException.class)
    public BaseResult handleServiceException(ServiceNotFoundException e) {
        String msg = "服务内部异常：" + e.getMessage();
        log.error(msg, e);
        return BaseResult.error(msg);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e) {
        String msg = "服务内部异常！" + e.getMessage();
        log.error(msg, e);
        return BaseResult.error(msg);
    }

    /**
     * 处理参数绑定异常，并拼接出错的参数异常信息
     * </p>
     *
     * @param result
     */
    private String handleBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            final List<FieldError> fieldErrors = result.getFieldErrors();
            return fieldErrors.iterator().next().getDefaultMessage();
        }
        return null;
    }
}
