package com.roc.cloud.db.service;

import com.roc.cloud.db.entity.TenantDataSourceInfo;

/**
 * @ClassName: DataSourceService
 * @Description:
 * @author: Roc
 * @date: 2020/9/24 21:19
 **/
public interface DataSourceService {

    /**
     * 通过租户获取连接信息
     * @param tenant :
     * @return
     * @throws
     * @author Roc
     * @date 2020/9/25
     **/
    TenantDataSourceInfo getTenantDataSourceInfo(String tenant);
}
