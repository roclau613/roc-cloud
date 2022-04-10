package com.roc.cloud.db.tenant;

import com.roc.cloud.db.service.DataSourceService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 多租户配置
 *
 * @author: Roc
 * @date: 2020/9/9 18:06
 */
public class TenantWebMvcConfigure implements WebMvcConfigurer {


    private DataSourceService dataSourceService;

    public TenantWebMvcConfigure(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    public TenantWebMvcConfigure() {

    }

    /**
     * 注册 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor handlerInterceptor = getHandlerInterceptor();
        if (handlerInterceptor != null) {
            registry.addInterceptor(handlerInterceptor)
                    .addPathPatterns("/**")
                    .order(-19);
        }
    }

    protected HandlerInterceptor getHandlerInterceptor() {
        return new TenantContextHandlerInterceptor(dataSourceService);
    }

}
