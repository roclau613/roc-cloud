package com.roc.cloud.db.tenant;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.roc.cloud.db.datasource.DbContextHolder;
import com.roc.cloud.db.entity.TenantDataSourceInfo;
import com.roc.cloud.db.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户信息解析器 用于将请求头中的租户编码和数据库名 封装到当前请求的线程变量中
 *
 * @author: Roc
 * @date: 2020/9/9 18:06
 */
@Slf4j
public class TenantContextHandlerInterceptor extends HandlerInterceptorAdapter {

    private static Map<String, TenantDataSourceInfo> connectionInfoMap = new ConcurrentHashMap<String, TenantDataSourceInfo>();

    private DataSourceService dataSourceService;

    public TenantContextHandlerInterceptor(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        String tenant = this.getHeader(request, HeaderConstant.TENANT_ID_HEADER);
        if (StrUtil.isNotEmpty(tenant) && null != dataSourceService) {
            TenantContextHandler.setTenant(tenant);
            log.debug("tenant id => {}", tenant);
            TenantDataSourceInfo tenantDataSourceInfo = connectionInfoMap.get(tenant);
            if (null == tenantDataSourceInfo) {
                log.info("not found datasosurce in container, go to company server to get and create => {}", tenant);
                tenantDataSourceInfo = dataSourceService.getTenantDataSourceInfo(tenant);
                if (null != tenantDataSourceInfo) {
                    connectionInfoMap.put(tenant, tenantDataSourceInfo);
                }
            }
            DbContextHolder.setDbType(tenantDataSourceInfo);
        }
        return super.preHandle(request, response, handler);
    }

    private String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StrUtil.isEmpty(value)) {
            value = request.getParameter(name);
        }
        if (StrUtil.isEmpty(value)) {
            return StrUtil.EMPTY;
        }
        return URLUtil.decode(value);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        TenantContextHandler.clear();
        DbContextHolder.clear();
        super.afterCompletion(request, response, handler, ex);
    }

}
