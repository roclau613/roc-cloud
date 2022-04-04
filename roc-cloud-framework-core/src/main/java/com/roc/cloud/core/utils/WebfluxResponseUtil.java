package com.roc.cloud.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.roc.cloud.common.model.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * WebfluxResponseUtil
 *
 * @author: Roc
 * @date 2020/5/5
 * * <p>
 */
public class WebfluxResponseUtil {
    /**
     * webflux的response返回json对象
     *
     * @param exchange   :
     * @param httpStatus :
     * @param msg        :
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author Roc
     * @date 2020/9/30
     **/
    public static Mono<Void> responseWriter(ServerWebExchange exchange, int httpStatus, String msg) {
        Result result = Result.of(null, httpStatus, msg);
        return responseWrite(exchange, httpStatus, result);
    }

    /**
     * responseFailed
     *
     * @param exchange :
     * @param msg      :
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author Roc
     * @date 2020/9/30
     **/
    public static Mono<Void> responseFailed(ServerWebExchange exchange, String msg) {
        Result result = Result.failed(msg);
        return responseWrite(exchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), result);
    }

    /**
     * responseFailed
     *
     * @param exchange   :
     * @param httpStatus :
     * @param msg        :
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author Roc
     * @date 2020/9/30
     **/
    public static Mono<Void> responseFailed(ServerWebExchange exchange, int httpStatus, String msg) {
        Result result = Result.failed(msg);
        return responseWrite(exchange, httpStatus, result);
    }

    /**
     * responseWrite
     *
     * @param exchange   :
     * @param httpStatus :
     * @param result     :
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author Roc
     * @date 2020/9/30
     **/
    public static Mono<Void> responseWrite(ServerWebExchange exchange, int httpStatus, Result result) {
        if (httpStatus == 0) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setAccessControlAllowCredentials(true);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.setStatusCode(HttpStatus.valueOf(httpStatus));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes(Charset.defaultCharset()));
        return response.writeWith(Mono.just(buffer)).doOnError((error) -> {
            DataBufferUtils.release(buffer);
        });
    }
}
