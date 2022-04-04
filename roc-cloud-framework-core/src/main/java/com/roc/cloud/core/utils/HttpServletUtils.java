package com.roc.cloud.core.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * httpServlet帮助类 <br>
 *
 * @date: 2020/10/27 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class HttpServletUtils {

    /**
     * 获取请求Request
     *
     * @return HttpServletRequest
     * @author Roc
     * @date 2020/10/27
     **/
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    /**
     * 获取请求Response
     *
     * @return javax.servlet.http.HttpServletResponse
     * @author Roc
     * @date 2020/10/27
     **/
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            return attributes.getResponse();
        }
        return null;
    }
}
