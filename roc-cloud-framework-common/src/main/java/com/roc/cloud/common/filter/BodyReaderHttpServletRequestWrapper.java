package com.roc.cloud.common.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 * @ClassName: BodyReaderHttpServletRequestWrapper
 * @Description: requestBody包装类
 * @Author: Roc
 * @date: 2020/11/21
 **/
@Slf4j
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 自定义消息头
     */
    private final Map<String, String> customHeaders;

    private byte[] body;

    /**
     * 构造方法，继承父类
     *
     * @param request :
     * @return
     * @Author: Roc
     * @date: 2020/11/21
     **/
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = IOUtils.toByteArray(new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
        this.customHeaders = new ConcurrentHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerKey = "";
        while (headerNames.hasMoreElements()) {
            headerKey = headerNames.nextElement();
            this.customHeaders.put(headerKey, request.getHeader(headerKey));
        }

    }

    /**
     * 重写，调用 getInputStream()
     *
     * @return java.io.BufferedReader
     * @Author: Roc
     * @date: 2020/11/21
     **/
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    /**
     * 添加自定义消息头
     *
     * @param name
     * @param value
     * @Author: Roc
     * @date: 2020/11/21
     */
    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    /**
     * 批量添加header头
     *
     * @param headers :
     * @Author: Roc
     * @date: 2020/11/21
     **/
    public void putHeaders(Map<String, String> headers) {
        this.customHeaders.putAll(headers);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }
        return Collections.enumeration(set);
    }

    /**
     * 重写，从缓存中获取流
     *
     * @return javax.servlet.ServletInputStream
     * @Author: Roc
     * @date: 2020/11/21
     **/
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }

}
