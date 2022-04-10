package com.roc.cloud.common.constant;

/**
 * @ClassName: HeaderConstant
 * @Description: 请求头常量
 * @author: Roc
 * @date: 2020/9/22 18:24
 **/
public interface HeaderConstant {

    /**
     * 租户id信息头
     */
    String TENANT_ID_HEADER = "roc-tenantId";
    /**
     * 请求机构id信息头
     */
    String COMPANY_ID = "x-companyId";
    /**
     * 请求日志id信息头
     */
    String TRACE_ID = "x-traceId";
    /**
     * 请求日志接口地址信息头
     */
    String REQUEST_URL = "x-requestUrl";

    /**
     * 头部灰度版本号key，统一入口由网关+机构/租户中心配置，配置中心的元数据的matedate保持一致
     */
    String CANARY_VERSION_HEADER = "roc-version";

    /**
     * 如果请求IP不为空优先走IP可用实例
     */
    String CANARY_REQUESTIP_HEADER = "roc-request-ip";
}
