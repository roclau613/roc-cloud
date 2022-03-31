package com.roc.cloud.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

/**
 * @ClassName: BodyReaderFilter
 * @Description: 包装request中的body
 * @Author: Roc
 * @date: 2020/11/21
 **/
public class BodyReaderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * 转换请求对象
     *
     * @param request :
     * @param response :
     * @param chain :
     * @return void
     * @Author: Roc
     * @date: 2020/11/21
     **/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request instanceof HttpServletRequest && StringUtils
            .contains(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
