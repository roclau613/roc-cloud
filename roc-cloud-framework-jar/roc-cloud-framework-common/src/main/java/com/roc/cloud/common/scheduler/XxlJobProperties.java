package com.roc.cloud.common.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 定时任务属性配置项 <br>
 *
 * @date: 2021/1/21 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
@ConfigurationProperties(prefix = "xxl.job")
@Setter
@Getter
public class XxlJobProperties {

    private String adminAddresses;

    private String accessToken;

    private XxlJobExecutorProperties executor = new XxlJobExecutorProperties();

    @Setter
    @Getter
    public static class XxlJobExecutorProperties {

        private String appname;

        private String address;

        private String ip;

        private int port;

        private String logPath;

        private int logRetentionDays;
    }
}
