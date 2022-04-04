package com.roc.cloud.dbfeign;

import com.roc.cloud.db.entity.TenantDataSourceInfo;

/**
 * @className: CompanyClient
 * @description: 机构服务feign
 * @author: Roc
 * @date: 2020/9/29
 **/
public interface CompanyDataSourceClient {

    /***
     * 获取数据源连接信息
     * @param appName : 服务名
     * @param companyId : 机构Id
     *
     * @return
     * @author Roc
     * @date 2020/9/29
     **/
    TenantDataSourceInfo findTenantDataSourceInfo(String appName, String companyId);

}
