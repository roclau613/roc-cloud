package com.roc.cloud.canary.gateway.starter.tenant;

import org.springframework.web.server.ServerWebExchange;

/**
 * @ClassName: TenantService
 * @Description: 租户服务
 * @author: Roc
 * @date: 2020/9/21 9:05
 **/
public interface TenantService {

    /**
     * 获取灰度服务版本标识
     *
     * @param exchange
     * @return
     */
    String getVersion(ServerWebExchange exchange);

    /**
     * 获取租户当前的版本号
     *
     * @param tenantId
     * @return
     */
    String getVersion(String tenantId);
}
