package com.roc.cloud.canary.ribbon.nacos.config;

import com.roc.cloud.canary.ribbon.nacos.interceptor.CanaryFeignInterceptor;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * restTemplate 拦截添加
 *
 * @author Roc
 * @version 1.0, 2020/5/8 10:26
 */
@Configuration
@Slf4j
public class CanaryFeignInterceptorConfigurer {

    @Bean
    @Scope("prototype")
    public RequestInterceptor feignRequestInterceptor() {
        log.info("初始化灰度Feign拦截..CanaryFeignInterceptorConfigurer");
        return new CanaryFeignInterceptor();
    }
}
