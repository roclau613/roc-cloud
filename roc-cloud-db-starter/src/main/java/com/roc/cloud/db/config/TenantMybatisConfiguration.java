package com.roc.cloud.db.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.roc.cloud.db.common.TenantEnum;
import com.roc.cloud.db.parsers.TenantSchemeHandler;
import com.roc.cloud.db.service.DataSourceService;
import com.roc.cloud.db.tenant.TenantContextHandler;
import com.roc.cloud.db.tenant.TenantWebMvcConfigure;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @ClassName: DynamicMybatisConfiguration
 * @Description: 多租户
 * @author: Roc
 * @date: 2020/9/9 13:55
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "roc.cloud.tenant.enabled", havingValue = "true")
@ComponentScan(basePackages = "com.roc.cloud.database")
public class TenantMybatisConfiguration {

    /**
     * 租户
     */
    @Value("${roc.cloud.tenant.type:schema}")
    private String tenant;

    /**
     * 租户字段
     */
    @Value("${roc.cloud.tenant.column:}")
    private String tenantColumn;


    /**
     * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
     */
    private static final Long MAX_LIMIT = 1000L;

    /**
     * 分页插件，自动识别数据库类型 多租户，请参考官网【插件扩展】
     */
    @Order(5)
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        log.info("tenant mode start for you => [{}]", tenant);
        //动态"表名" 插件 来实现 租户schema切换 加入解析链
        if (TenantEnum.SCHEMA.eq(tenant)) {
            TenantSchemeHandler tenantSchemeHandler = new TenantSchemeHandler();
            interceptor.addInnerInterceptor(tenantSchemeHandler);
        } else if (TenantEnum.COLUMN.eq(tenant)) {
            TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(new TenantLineHandler() {
                /**
                 * 获取租户ID
                 * @return
                 */
                @Override
                public Expression getTenantId() {
                    String tenant = TenantContextHandler.getTenant();
                    if (tenant != null) {
                        return new StringValue(tenant);
                    }
                    return new NullValue();
                }

                /**
                 * 获取多租户的字段名
                 * @return String
                 */
                @Override
                public String getTenantIdColumn() {
                    return tenantColumn;
                }

                /**
                 * 过滤不需要根据租户隔离的表
                 * 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
                 * @param tableName 表名
                 */
                @Override
                public boolean ignoreTable(String tableName) {
                    // return tenantProperties.getIgnoreTables().stream().anyMatch(
                    //     (t) -> t.equalsIgnoreCase(tableName)
                    // );
                    return false;
                }
            });
            interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        }

        //分页插件: PaginationInnerInterceptor
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return interceptor;
    }


    /**
     * gateway 网关模块需要禁用 spring-webmvc 相关配置，必须通过在类上面加限制条件方式来实现， 不能直接Bean上面加
     */
    @ConditionalOnProperty(prefix = "roc.cloud.tenant.webmvc", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class WebMvcConfig {

        @Bean
        @ConditionalOnProperty(prefix = "roc.cloud.tenant.webmvc", name = "enabled", havingValue = "true", matchIfMissing = true)
        @ConditionalOnBean(DataSourceService.class)
        public TenantWebMvcConfigure getTenantWebMvcConfigurer(DataSourceService dataSourceService) {
            log.info("init tenantInfo of interceptor ...");
            return new TenantWebMvcConfigure(dataSourceService);
        }

    }

}
