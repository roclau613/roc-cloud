package com.roc.cloud.common.filter;

import cn.hutool.core.util.StrUtil;
import com.roc.cloud.common.constant.CommonConstant;
import com.roc.cloud.common.constant.HeaderConstant;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 机构日志id过滤器
 *
 * @author: Roc
 * @date 2019/9/15
 */
@ConditionalOnClass(Filter.class)
public class TenantFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String companyId = request.getHeader(HeaderConstant.COMPANY_ID);
            String traceId = request.getHeader(HeaderConstant.TRACE_ID);
            String requestUrl = request.getHeader(HeaderConstant.REQUEST_URL);
            if (StrUtil.isNotEmpty(companyId)) {
                MDC.put(CommonConstant.LOG_COMPANY_ID, companyId);
            }
            if (StrUtil.isNotEmpty(traceId)) {
                MDC.put(CommonConstant.LOG_TRACE_ID, traceId);
            }
            if (StrUtil.isNotEmpty(requestUrl)) {
                MDC.put(CommonConstant.LOG_REQUEST_URL, requestUrl);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
