package com.roc.cloud.log.config;

import com.roc.cloud.log.properties.AuditLogProperties;
import com.roc.cloud.log.properties.LogDbProperties;
import com.roc.cloud.log.properties.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 日志自动配置
 *
 * @author: Roc
 * @date 2019/8/13
 */
@EnableConfigurationProperties({TraceProperties.class, AuditLogProperties.class})
public class LogAutoConfigure {

    /**
     * 日志数据库配置
     *
     * @author Roc
     * @ConditionalOnClass(HikariConfig.class)
     * @date 2020/11/6
     **/

    @Configuration
    @EnableConfigurationProperties(LogDbProperties.class)
    public static class LogDbAutoConfigure {
    }
}
