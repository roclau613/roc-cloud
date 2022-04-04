package com.roc.cloud.canary.gateway.starter.balancer;

import java.util.Arrays;
import java.util.List;

/**
 * <br>
 *
 * @date: 2021/08/24 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class CanaryLoadBalancerConstant {

    /**
     * 入口IP
     */
    public static final String ORIGINAL_FORWARDED_HOST_FOR = "X-original-Forwarded-Host-For";

    /**
     * 灰度环境
     */
    public static final List<String> PROFILES = Arrays.asList("dev");
}
