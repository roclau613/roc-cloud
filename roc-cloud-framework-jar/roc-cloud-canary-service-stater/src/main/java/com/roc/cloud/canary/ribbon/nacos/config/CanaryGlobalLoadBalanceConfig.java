package com.roc.cloud.canary.ribbon.nacos.config;

import com.netflix.loadbalancer.IRule;
import com.roc.cloud.canary.ribbon.nacos.rule.CanaryNacosRibbonRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: CanaryGlobalLoadBalanceConfig
 * @Description: 全局路由规则配置
 * @author: Roc
 * @date: 2020/9/18 11:57
 **/
@Configuration
@ConditionalOnProperty(name = "roc.cloud.canary.enabled", havingValue = "true")
@ComponentScan(basePackages = "com.roc.cloud.canary.ribbon")
@Slf4j
public class CanaryGlobalLoadBalanceConfig {

    @Bean
    @Scope("prototype")
    public IRule canaryRule() {
        log.info("初始化灰度路由规则..CanaryNacosRibbonRule");
        return new CanaryNacosRibbonRule();
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(RestTemplateProperties restTemplateProperties) {
        log.warn("未找到RestTemplate..初始化默认的RestTemplate");
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(restTemplateProperties.getConnectTimeout());
        factory.setReadTimeout(restTemplateProperties.getReadTimeout());
        return new RestTemplate(factory);
    }
}
