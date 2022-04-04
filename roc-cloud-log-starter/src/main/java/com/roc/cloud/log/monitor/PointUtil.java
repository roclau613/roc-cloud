package com.roc.cloud.log.monitor;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 审计日志
 *
 * @author: Roc
 * @date 2020/2/3
 * <p>
 */
@Setter
@Getter
public class ExceptionAudit {

    /**
     * 索引名称 对应枚举类EsIndexNameEnum name
     */
    private String indexName;
    /**
     * 审核ID
     */
    private String auditId;
    /**
     * 清楚参数
     */
    private String params;
    /**
     * 应用名称
     */
    private String exApplicationName;

    /**
     * 业务系统名称
     */
    private String businessType;

    /**
     * 类名称
     */
    private String exClassName;

    /**
     * 方法名称
     */
    private String exMethodName;

    /**
     * 机构id
     */
    private String companyId;

    /**
     * 日志渠道类型
     */
    private String channelType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 操作名称
     */
    private String processName;
    /**
     * ip
     */
    private String ip;

    /**
     * 操作时间
     */
    private LocalDateTime timestamp;
    /**
     * 操作信息
     */
    private String operation;

    /**
     * 异常信息
     */
    private  String exceptionMsg;
}
