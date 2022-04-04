package com.roc.cloud.canary.ribbon.nacos.interceptor;

import com.roc.cloud.canary.ribbon.nacos.context.CanaryContext;
import com.roc.cloud.canary.ribbon.nacos.context.CanaryFilterContextHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求头拦截，仅针对webmvc的
 *
 * <p>
 * 实现WebMvcConfigurer.addInterceptors
 * <p>
 * registry.addInterceptor(getInterceptor()).addPathPatterns("/**");
 * </p>
 *
 * @author Roc
 * @version 1.0, 2020/5/8 08:55
 */
@Slf4j
public class CanaryRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取头部header
        CanaryContext canaryContext = CanaryFilterContextHolder.getCurrentContext();
        String version = request.getHeader(HeaderConstant.CANARY_VERSION_HEADER);
        if (StringUtils.isNotBlank(version)) {
            log.debug("1. CanaryRequestInterceptor(webmvc)触发灰度拦截，当前版本：{}", version);
            canaryContext.add(HeaderConstant.CANARY_VERSION_HEADER, version);
        }
        String tenantId = request.getHeader(HeaderConstant.TENANT_ID_HEADER);
        if (StringUtils.isNotBlank(tenantId)) {
            log.debug("1. CanaryRequestInterceptor(webmvc)触发灰度拦截，当前租户信息：{}", tenantId);
            canaryContext.add(HeaderConstant.TENANT_ID_HEADER, tenantId);
        }
        String requestIp = request.getHeader(HeaderConstant.CANARY_REQUESTIP_HEADER);
        if (StringUtils.isNotBlank(requestIp)) {
            log.debug("1. CanaryRequestInterceptor(webmvc)触发灰度拦截，当前IP信息：{}", requestIp);
            canaryContext.add(HeaderConstant.CANARY_REQUESTIP_HEADER, requestIp);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            CanaryFilterContextHolder.clearCurrentContext();
        } catch (Exception exception) {
            log.error("CanaryFilterContextHolder clearCurrentContext error", exception);
        }
    }
}
