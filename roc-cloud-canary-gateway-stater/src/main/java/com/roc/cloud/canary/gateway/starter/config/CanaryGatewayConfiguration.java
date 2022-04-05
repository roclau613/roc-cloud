package com.roc.cloud.canary.gateway.starter.config;

import com.roc.cloud.canary.gateway.starter.filter.CanaryLoadBalancerFilter;
import com.roc.cloud.canary.gateway.starter.tenant.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: CanaryGatewayConfig
 * @Description: 初始化
 * @author: Roc
 * @date: 2020/9/21 9:23
 **/
@Configuration
@ConditionalOnProperty(name = "roc.cloud.canary.enabled", havingValue = "true")
@Slf4j
public class CanaryGatewayConfiguration {

    /**
     * 初始化灰度过滤器
     *
     * @param loadBalancerClientFactory :
     * @param loadBalancerProperties    :
     * @param applicationContext        : 租户服务
     * @return
     * @author Roc
     * @date 9:27 2020/9/21
     **/
    @Bean
    public CanaryLoadBalancerFilter canaryLoadBalancerFilter(LoadBalancerClientFactory loadBalancerClientFactory,
                                                             LoadBalancerProperties loadBalancerProperties, ApplicationContext applicationContext) {
        log.info("初始化灰度过滤器CanaryLoadBalancerFilter");
        TenantService tenantService = null;
        try {
            tenantService = applicationContext.getBean(TenantService.class);
        } catch (BeansException e) {
            log.warn("当前环境未启用租户组件");
        }
        return new CanaryLoadBalancerFilter(loadBalancerClientFactory, loadBalancerProperties, tenantService);
    }
}
