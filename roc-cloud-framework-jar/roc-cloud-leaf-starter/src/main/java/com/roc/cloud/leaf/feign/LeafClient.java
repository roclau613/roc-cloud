package com.roc.cloud.leaf.feign;

import com.roc.cloud.leaf.constants.Constant;
import com.roc.cloud.leaf.feign.fallback.LeafClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @className: LeafClient
 * @description: 唯一id获取
 * @Author: Roc
 * @date: 2020/9/22
 **/
@FeignClient(name = Constant.LEAF_SERVICE, fallbackFactory = LeafClientFallbackFactory.class, decode404 = true)
public interface LeafClient {

    /**
     * 获取serviceId
     *
     * @param key : 数据库配置的applicationName
     * @return java.lang.String
     * @throws
     * @author liupeng
     * @date 2020/9/22
     **/
    @GetMapping(value = "/leaf-server/api/segment/get/{key}")
    String creatSegmentId(@PathVariable("key") String key);

}
