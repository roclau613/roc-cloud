package com.roc.cloud.core.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * feign 公共错误解码器 <br>
 *
 * @date: 2020/9/29 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
@Slf4j
public class CommonApiFeignErrorDecoder implements ErrorDecoder {

    private static final int NOT_FOUND_CODE = 400;
    private static final int ERROR_500_CODE = 500;
    private static final String FIELD_ERR_LEVEL = "errLevel";
    private static final String FIELD_CODE = "code";
    private static final String FIELD_MSG = "msg";

    /**
     * 拦截feign调用服务端返回apiexception公共异常的Result，全局拦截异常后需要设置Http的状态码未500，反之该拦截不起作用
     *
     * @param methodKey
     * @param response
     * @return
     */
    @Override
    public Exception decode(String methodKey, Response response) {

        Response.Body body = response.body();

        if (response == null || body == null) {
            return new PlatformApiException(CodeEnum.ERROR);
        }

        String requestUrl = response.request().url();

        int responseStatus = response.status();
        if (responseStatus >= NOT_FOUND_CODE && responseStatus < ERROR_500_CODE) {
            log.error("请求Url：{}，服务提供方返4xx错误，错误码：{}！", requestUrl, responseStatus);
            return new PlatformApiException(CodeEnum.REMOTE_SERVICE_NOT_FOUND);
        }
        String responseBody = "";
        try {
            responseBody = Util.toString(body.asReader());
        } catch (IOException e) {
            log.error("获取远程调用响应流出现异常，请求uri:{}", requestUrl);
            return new PlatformApiException(CodeEnum.REMOTE_SERVICE_ERROR);
        }
        if (StringUtils.isEmpty(responseBody)) {
            log.error("获取远程调用响应流出现异常，请求uri:{}", requestUrl);
            return new PlatformApiException(CodeEnum.REMOTE_SERVICE_ERROR);
        }

        String errorLogText = responseBody;
        if (!StringUtils.isEmpty(errorLogText)) {
            errorLogText = errorLogText.replaceAll("\"", "");
        }
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (jsonObject == null) {
            return new PlatformApiException(CodeEnum.REMOTE_SERVICE_ERROR);
        }
        if (Objects.equals(1, jsonObject.getInteger(FIELD_ERR_LEVEL))) {
            log.error("请求Url：{}服务提供方返回异常结果为：{}", requestUrl, errorLogText);
        } else {
            log.warn("请求Url：{}服务提供方返回异常结果为：{}", requestUrl, errorLogText);
        }
        return new PlatformApiException(jsonObject.getInteger(FIELD_CODE), jsonObject.getString(FIELD_MSG));
    }

}

