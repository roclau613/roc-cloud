package com.roc.cloud.canary.ribbon.nacos.context;

/**
 * @ClassName: TenantContextHolder
 * @Description: 租户上下文
 * @author: Roc
 * @date: 2020/9/21 19:52
 **/
public class TenantContextHolder {


    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * Retrieves the current thread bound .
     *
     * @return the current context
     */
    public static String getCurrentContext() {
        return contextHolder.get();
    }

    /**
     * 设置上下文信息
     *
     * @param tenant
     */
    public static void setTenant(String tenant) {
        contextHolder.set(tenant);
    }

    /**
     * Clears the current context.
     */
    public static void clearCurrentContext() {
        contextHolder.remove();
    }
}
