package com.roc.cloud.leaf;

import com.roc.cloud.leaf.constants.Constant;
import com.roc.cloud.leaf.feign.LeafClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @className: LeafAutoConfiguration
 * @description:
 * @Author: Roc
 * @date: 2020/09/22
 **/
@EnableFeignClients(basePackageClasses = {LeafClient.class})
public class LeafAutoConfiguration {

    /**
     * http模式获取唯一id.
     *
     * @param environment :
     * @return
     * @throws
     * @author liupeng
     * @date 2020/9/22
     **/
    @Bean
    @ConditionalOnProperty(name = Constant.CONFIG_LEAF_SEGMENT_HTTP_ENABLE, havingValue = "true")
    public LeafIdGenerator leafIdGenerator(Environment environment) {
        return new LeafIdGenerator(environment);
    }
}
