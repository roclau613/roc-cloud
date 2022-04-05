package com.roc.cloud.core.advice;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.roc.cloud.core.exception.IdempotencyException;
import com.roc.cloud.core.exception.PlatformApiException;
import com.roc.cloud.core.exception.ServiceException;
import com.roc.cloud.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 异常通用处理
 *
 * @author: Roc
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionAdvice {

    protected static final String ERROR_MSG = "服务繁忙，请稍后重试";

    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public Result badRequestException(HttpServletRequest request, IllegalArgumentException e) {
        return defErrorHandler(request.getRequestURI(), "参数解析失败", e);
    }

    /**
     * MethodArgumentNotValidException异常处理返回json
     * 返回状态码:400
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result badRequestException(HttpServletRequest request, MethodArgumentNotValidException e) {
        return defErrorHandler(request.getRequestURI(), e.getBindingResult().getFieldError().getDefaultMessage(), e);
    }

    /**
     * 返回状态码:405
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return defErrorHandler(request.getRequestURI(), "不支持当前请求方法", e);
    }

    /**
     * 返回状态码:405
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result handleMissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException e) {
        return defErrorHandler(request.getRequestURI(), "参数转换失败", e);
    }

    /**
     * 返回状态码:415
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Result handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        return defErrorHandler(request.getRequestURI(), "不支持当前媒体类型", e);
    }

    /**
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public Result handleSqlException(HttpServletRequest request, SQLException e) {
        return defErrorHandler(request.getRequestURI(), ERROR_MSG, e);
    }

    /**
     * IOException  Broken pipe
     * 返回状态码:500
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Result ioExceptionHandler(IOException e, HttpServletRequest request) {
        String errorMsg = ExceptionUtils.getRootCauseMessage(e);
        if (StringUtils.containsIgnoreCase(errorMsg, "Broken pipe")) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();

            String content = sw.toString();
            WebhookBodyDto body = new WebhookBodyDto();
            body.setMsgtype("markdown");
            WebhookBodyDto.WebhookTextDto text = new WebhookBodyDto.WebhookTextDto();
            body.setMarkdown(text);
            text.setContent(content);
            String response = HttpUtil.post("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=bb695e23-6822-46ff-b8eb-e5bc1860d99b", JSONObject.toJSONString(body));
            log.info("ioExceptionHandler body:{}, response:{}", body, response);

            return defWarnHandler(request.getRequestURI(), e);
        } else {
            return defWarnHandler(request.getRequestURI(), e);
        }
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public Result handleException(HttpServletRequest request, ServiceException e) {
        return defWarnHandler(request.getRequestURI(), e.getErrCode(), e.getErrMsg(), e);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PlatformApiException.class)
    public Result handleException(HttpServletRequest request, PlatformApiException e) {
        if (Objects.equals(1, e.getErrLevel())) {
            return defErrorHandler(request.getRequestURI(), e.getMessage(), e);
        }
        return defWarnHandler(request.getRequestURI(), e.getErrCode(), e.getErrMsg(), e);
    }

    /**
     * IdempotencyException 幂等性异常
     * 返回状态码:200
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IdempotencyException.class)
    public Result handleException(HttpServletRequest request, IdempotencyException e) {
        return defErrorHandler(request.getRequestURI(), e.getMessage(), e);
    }


    /**
     * 所有异常统一处理
     * 返回状态码:500
     *
     * @param e :
     * @return com.roc.cloud.common.model.Result
     * @author Roc
     * @date 2020/11/19
     **/
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result handleException(HttpServletRequest request, Exception e) {
        return defErrorHandler(request.getRequestURI(), ERROR_MSG, e);
    }

    protected Result defErrorHandler(String url, String msg, Exception e) {
        log.error(msg + ", url: " + url, e);
        return Result.failed(msg);
    }

    protected Result defErrorHandler(String msg, Exception e) {
        log.error(msg, e);
        return Result.failed(msg);
    }

    private Result defErrorHandler(Integer errorCode, String msg, Exception e) {
        log.error(errorCode + ":" + msg, e);
        return Result.failed(errorCode, msg);
    }

    private Result defWarnHandler(String msg, Exception e) {
        log.warn(msg, e);
        return Result.failed(msg);
    }

    private Result defWarnHandler(String url, Integer errorCode, String msg, Exception e) {
        log.warn(errorCode + ":" + msg + ", url: " + url, e);
        return Result.failed(errorCode, msg);
    }

    private Result defWarnHandler(Integer errorCode, String msg, Exception e) {
        log.warn(errorCode + ":" + msg, e);
        return Result.failed(errorCode, msg);
    }

}
