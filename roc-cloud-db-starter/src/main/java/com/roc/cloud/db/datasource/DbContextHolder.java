package com.roc.cloud.db.datasource;

import com.roc.cloud.db.entity.TenantDataSourceInfo;

/**
 * @ClassName: DbContextHolder
 * @Description: 当前正在使用的数据源信息的线程上线文
 * @author: Roc
 * @date: 2020/9/24 11:24
 **/
public class DbContextHolder {

    private static final ThreadLocal<TenantDataSourceInfo> CONTEXT_HOLDER = new ThreadLocal<TenantDataSourceInfo>();

    public static void setDbType(TenantDataSourceInfo tenantDataSourceInfo) {
        CONTEXT_HOLDER.set(tenantDataSourceInfo);
    }

    public static TenantDataSourceInfo getDbType() {
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
