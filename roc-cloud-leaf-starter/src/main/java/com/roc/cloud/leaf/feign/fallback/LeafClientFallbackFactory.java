package com.roc.cloud.leaf.feign.fallback;

import com.roc.cloud.leaf.feign.LeafClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @className: LeafClientFallbackFactory
 * @description: 唯一id服务接口降级处理
 * @Author: Roc
 * @date: 2020/9/22
 **/
@Slf4j
public class LeafClientFallbackFactory implements FallbackFactory<LeafClient> {

    @Override
    public LeafClient create(Throwable cause) {
        return new LeafClient() {
            @Override
            public String creatSegmentId(String key) {
                return null;
            }
        };
    }
}
