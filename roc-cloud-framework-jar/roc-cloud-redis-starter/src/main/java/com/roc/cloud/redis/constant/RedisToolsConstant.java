package com.roc.cloud.redis.constant;

/**
 * redis 工具常量
 *
 * @author: Roc
 * @date 2018/5/21 11:59
 */
public class RedisToolsConstant {
    private RedisToolsConstant() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * single Redis
     */
    public final static int SINGLE = 1;

    /**
     * Redis cluster
     */
    public final static int CLUSTER = 2;
    /**
     * key  =  edu:course:addCourse:1001   val=xxxx
     * edu:服务名称 ，我定义了常量类RedisToolsConstant
     * course：项目下的模块名称
     * addCourse：具体使用redis方法名称
     * 1001：具体定义的key
     */
    public final static String PROJECT_PREFIX_AUDIT = "audit:%s:%s:%s";
    public final static String PROJECT_PREFIX_PERSONNEL = "personnel:%s:%s:%s";
    public final static String PROJECT_PREFIX_CRM = "crm:%s:%s:%s";
    public final static String PROJECT_PREFIX_EDU = "edu:%s:%s:%s";
    public final static String PROJECT_PREFIX_REPORT = "report:%s:%s:%s";
    public final static String PROJECT_PREFIX_MALL = "mall:%s:%s:%s";
    public final static String PROJECT_PREFIX_COMMODITY = "commodity:%s:%s:%s";
    public final static String PROJECT_PREFIX_COMPANY = "company:%s:%s:%s";
    public final static String PROJECT_PREFIX_DATALAKE = "datalake:%s:%s:%s";
    public final static String PROJECT_PREFIX_GATEWAY = "gateway:%s:%s:%s";
    public final static String PROJECT_PREFIX_LEAF = "leaf:%s:%s:%s";
    public final static String PROJECT_PREFIX_ORDER = "order:%s:%s:%s";
    public final static String PROJECT_PREFIX_CHANNEL = "channel:%s:%s:%s";
    public final static String PROJECT_PREFIX_FACE = "face:%s:%s:%s";
    /**
     * 账号中心
     */
    public final static String PROJECT_PREFIX_ACCOUNT = "account:%s:%s:%s";
}
