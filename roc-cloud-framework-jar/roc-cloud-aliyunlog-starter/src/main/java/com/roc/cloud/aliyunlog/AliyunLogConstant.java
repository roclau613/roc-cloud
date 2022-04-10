package com.roc.cloud.aliyunlog;

/**
 * <br>
 *
 * @date: 2021/08/24 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 2.0 <br>
 */
public class AliyunLogConstant {

    /**
     * 日志链路追踪id日志标志 .
     */
    public final static String LOG_TRACE_ID = "traceId";
    /**
     * 日志链路追踪id日志标志-请求url地址
     */
    public final static String LOG_REQUEST_URL = "requestUrl";

    /**
     * 日志链路追踪id日志标志-机构
     */
    public final static String LOG_COMPANY_ID = "companyId";

    public final static String LOG_CONFIG_PREFIX = "log.aliyun";

    public final static String LOG_PROJECT_CONFIG_PREFIX = LOG_CONFIG_PREFIX + ".project";

    public final static String LOG_PRODUCER_CONFIG_PREFIX = LOG_CONFIG_PREFIX + ".producer";

    public final static String LOG_CONFIG_LOGGER_FILTER = "logger.filter";

    public final static String DEFAULT_ROOT_LOGGER_NAME = "ROOT";
}
