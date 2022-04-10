package com.roc.cloud.canary.ribbon.nacos.interceptor;

import com.roc.cloud.canary.ribbon.nacos.context.CanaryContext;
import com.roc.cloud.canary.ribbon.nacos.context.CanaryFilterContextHolder;
import com.roc.cloud.common.constant.HeaderConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * feign拦截，将header信息保存到上下文中传递参数
 *
 * @author Roc
 * @version 1.0, 2020/5/8 09:46
 */
@Slf4j
public class CanaryFeignInterceptor implements RequestInterceptor {

    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        CanaryContext ribbonFilterContext = CanaryFilterContextHolder.getCurrentContext();
        String version = ribbonFilterContext.get(HeaderConstant.CANARY_VERSION_HEADER);
        if (StringUtils.isNotBlank(version)) {
            log.debug("2.CanaryFeignInterceptor设置灰度版本：{}到请求头中", version);
            template.header(HeaderConstant.CANARY_VERSION_HEADER, version);
        }
        String tenantId = ribbonFilterContext.get(HeaderConstant.TENANT_ID_HEADER);
        if (StringUtils.isNotBlank(tenantId)) {
            log.debug("2.CanaryFeignInterceptor设置租户信息：{}到请求头中", tenantId);
            template.header(HeaderConstant.TENANT_ID_HEADER, tenantId);
        }
        String requestIp = ribbonFilterContext.get(HeaderConstant.CANARY_REQUESTIP_HEADER);
        if (StringUtils.isNotBlank(requestIp)) {
            log.debug("2.CanaryFeignInterceptor设置灰度IP：{}到请求头中", requestIp);
            template.header(HeaderConstant.CANARY_REQUESTIP_HEADER, requestIp);
        }
    }
}
