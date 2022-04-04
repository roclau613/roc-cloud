package com.roc.cloud.db.service.impl;

import com.roc.cloud.db.entity.TenantDataSourceInfo;
import com.roc.cloud.dbfeign.CompanyDataSourceClient;
import com.roc.cloud.db.service.DataSourceService;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @ClassName: DataSourceServiceImpl
 * @Description:
 * @author: Roc
 * @date: 2020/9/24 21:19
 **/
@Service
@ConditionalOnProperty(name = "roc.cloud.tenant.enabled", havingValue = "true")
public class DataSourceServiceImpl implements DataSourceService {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private CompanyDataSourceClient companyDataSourceClient;

    /**
     * 通过租户获取连接信息
     *
     * @param tenant :
     *
     * @return
     * @throws
     * @author Roc
     * @date 2020/9/25
     **/
    @Override
    public TenantDataSourceInfo getTenantDataSourceInfo(String tenant) {
        TenantDataSourceInfo tenantDataSourceInfo = companyDataSourceClient.findTenantDataSourceInfo(appName, tenant);
        Assert.notNull(tenantDataSourceInfo,
                MessageFormat.format("companyClient.findTenantDataSourceInfo(appName:{0}, tenant:{1})", appName, tenant));
        return tenantDataSourceInfo;
    }

}
