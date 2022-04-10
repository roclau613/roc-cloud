package com.roc.cloud.log.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志数据源配置
 * logType=db时生效(非必须)，如果不配置则使用当前数据源
 *
 * @author: Roc
 * @date 2020/2/8
 * <p>
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "roc.cloud.audit-log.datasource")
public class LogDbProperties extends HikariConfig {
}
