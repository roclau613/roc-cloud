package com.roc.cloud.db.datasource;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.roc.cloud.db.entity.DataSourceInfo;
import com.roc.cloud.db.entity.DataSourceMasterSlaveEnum;
import com.roc.cloud.db.entity.TenantDataSourceInfo;
import com.roc.cloud.db.tenant.TenantContextHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.springframework.core.env.Environment;

/**
 * @ClassName: DynamicDataSource
 * @Description: 当前正在使用的数据源信息的线程上线文
 * @author: Roc
 * @date: 2020/9/24 14:54
 **/
@Slf4j
public class DynamicDataSource extends AbstractDynamicDataSource<ShardingDataSource> {

    private boolean testWhileIdle = true;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;

    /**
     * 是否打开连接泄露自动检测
     */
    private boolean removeAbandoned = false;
    /**
     * 连接长时间没有使用，被认为发生泄露时长
     */
    private long removeAbandonedTimeoutMillis = 300 * 1000;
    /**
     * 发生泄露时是否需要输出 log，建议在开启连接泄露检测时开启，方便排错
     */
    private boolean logAbandoned = false;

    // 只要maxPoolPreparedStatementPerConnectionSize>0,poolPreparedStatements就会被自动设定为true，使用oracle时可以设定此值。
    // private int maxPoolPreparedStatementPerConnectionSize = -1;

    /**
     * 配置监控统计拦截的filters
     */
    private String filters;
    /**
     * 监控统计："stat" 防SQL注入："wall" 组合使用： "stat,wall"
     */
    private List<Filter> filterList;

    private static ExecutorService threadPool = new ThreadPoolExecutor(
            3,
            5,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(15),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public DataSource createDataSource(TenantDataSourceInfo tenantDataSourceInfo)
            throws SQLException {
        Environment environment = getApplicationContext().getEnvironment();
        String property = environment.getProperty("roc.cloud.dynamic.sharding.enabled");
        Boolean shardingEnable = Boolean.valueOf(property);
        DataSource dataSource = buildDefaultDataSource(tenantDataSourceInfo.getMasterDataSource());
        ;
        if (shardingEnable) {
            threadPool.execute(() -> {
                try {
                    DataSource tempDataSource = buildShardingDataSource(tenantDataSourceInfo);
                    super.addTargetDataSource(tenantDataSourceInfo.getTenant(), tempDataSource);
                } catch (SQLException e) {
                    log.error("buildShardingDataSource fail: ", e);
                }
            });
        }
        return dataSource;
    }

    private ShardingDataSource buildShardingDataSource(TenantDataSourceInfo tenantDataSourceInfo) throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        Properties properties = getProperties();
        buildMasterSlaveRuleConfigs(tenantDataSourceInfo, shardingRuleConfig, dataSourceMap);
        long start = System.currentTimeMillis();
        ShardingDataSource dataSource = (ShardingDataSource) ShardingDataSourceFactory
                .createDataSource(dataSourceMap, shardingRuleConfig, properties);
        log.info("dataSource init, create times => {} ms", System.currentTimeMillis() - start);
        return dataSource;
    }

    private void buildMasterSlaveRuleConfigs(TenantDataSourceInfo tenantDataSourceInfo,
                                             ShardingRuleConfiguration shardingRuleConfig, Map<String, DataSource> dataSourceMap) {
        List<DataSourceInfo> dataSourceInfoList = tenantDataSourceInfo.getSlaveDataSourceList();
        Collection<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs = shardingRuleConfig.getMasterSlaveRuleConfigs();
        List<String> slaveDataSourceNames = new ArrayList<>(dataSourceInfoList.size());
        DataSourceInfo masterDataSource = tenantDataSourceInfo.getMasterDataSource();
        dataSourceMap.put(DataSourceMasterSlaveEnum.MASTER.getName(), buildDefaultDataSource(masterDataSource));
        for (int i = 0; i < dataSourceInfoList.size(); i++) {
            DataSourceInfo dataSourceInfo = dataSourceInfoList.get(i);
            String slaveDataSourceName = DataSourceMasterSlaveEnum.SLAVE.getName() + i;
            slaveDataSourceNames.add(slaveDataSourceName);
            dataSourceMap.put(slaveDataSourceName, buildDefaultDataSource(dataSourceInfo));
        }
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration(
                tenantDataSourceInfo.getTenant(),
                DataSourceMasterSlaveEnum.MASTER.getName(),
                slaveDataSourceNames);
        masterSlaveRuleConfigs.add(masterSlaveRuleConfig);
        log.info("buildMasterSlaveRuleConfigs end");
    }

    private Properties getProperties() {
        Properties shardingProperties = new Properties();
        shardingProperties.put("sql.show", true);
        return shardingProperties;
    }

    @Override
    public DataSource createDataSource(String driverClassName, String url, String username, String password) {
        return null;
    }

    /**
     * 创建数据源，这里创建的数据源是带有连接池属性的
     *
     * @see
     */
    public DruidDataSource buildDefaultDataSource(DataSourceInfo dataSourceInfo) {
        log.info("create druidDataSource ...", TenantContextHandler.getTenant());
        DruidDataSource defaultDataSource = (DruidDataSource) targetDataSources.get(DEFAULT_DATASOURCE_KEY);
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(dataSourceInfo.getUrl());
        ds.setUsername(dataSourceInfo.getUsername());
        ds.setPassword(dataSourceInfo.getPassword());
        ds.setDriverClassName(dataSourceInfo.getDriver());
        ds.setInitialSize(defaultDataSource.getInitialSize());
        ds.setMinIdle(defaultDataSource.getMinIdle());
        ds.setMaxActive(defaultDataSource.getMaxActive());
        ds.setMaxWait(defaultDataSource.getMaxWait());
        ds.setTimeBetweenConnectErrorMillis(defaultDataSource.getTimeBetweenConnectErrorMillis());
        ds.setTimeBetweenEvictionRunsMillis(defaultDataSource.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(defaultDataSource.getMinEvictableIdleTimeMillis());

        ds.setValidationQuery(defaultDataSource.getValidationQuery());
        ds.setTestWhileIdle(testWhileIdle);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestOnReturn(testOnReturn);

        ds.setRemoveAbandoned(removeAbandoned);
        ds.setRemoveAbandonedTimeoutMillis(removeAbandonedTimeoutMillis);
        ds.setLogAbandoned(logAbandoned);

        ds.setMaxPoolPreparedStatementPerConnectionSize(defaultDataSource.getMaxPoolPreparedStatementPerConnectionSize());

        if (StrUtil.isNotBlank(filters)) {
            try {
                ds.setFilters(filters);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        addFilterList(ds);
        return ds;
    }

    private void addFilterList(DruidDataSource ds) {
        if (filterList != null) {
            List<Filter> targetList = ds.getProxyFilters();
            for (Filter add : filterList) {
                boolean found = false;
                for (Filter target : targetList) {
                    if (add.getClass().equals(target.getClass())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    targetList.add(add);
                }
            }
        }
    }

    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }

    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        // 允许一次执行多条语句
        config.setMultiStatementAllow(true);
        // 允许非基本语句的其他语句
        config.setNoneBaseStatementAllow(true);
        return config;
    }
}
