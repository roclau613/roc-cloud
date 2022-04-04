package com.roc.cloud.leaf.constants;

/**
 * @className: Constant
 * @description: 常量类
 * @author: Roc
 * @date: 2020/9/23
 **/
public interface Constant {

    /**
     * 唯一id服务
     */
    String LEAF_SERVICE = "roc-leaf";
    /**
     * leaf前缀
     **/
    String LEAF_SEGMENT_PREFIX = "roc.cloud.leaf.segment";
    /**
     * 是否开启leafId的feignClient模式获取
     */
    String CONFIG_LEAF_SEGMENT_HTTP_ENABLE = LEAF_SEGMENT_PREFIX + ".http.enable";
    /**
     * http模式获取唯一的请求基本路径
     **/
    String CONFIG_LEAF_SEGMENT_HTTP_BASE_URL = LEAF_SEGMENT_PREFIX + ".http.base-url";
    /**
     * id中心 获取失败会返回字符串”0“的ID
     */
    String LEAF_ZERO_ID = "0";
}
