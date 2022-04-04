package com.roc.cloud.log.service.impl;

import com.roc.cloud.log.model.Audit;
import com.roc.cloud.log.model.ExceptionAudit;
import com.roc.cloud.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.format.DateTimeFormatter;

/**
 * 审计日志实现类-打印日志
 *
 * @author: Roc
 * @date 2020/2/3
 * <p>


 */
@Slf4j
@ConditionalOnProperty(name = "roc.cloud.audit-log.log-type", havingValue = "logger", matchIfMissing = true)
public class LoggerAuditServiceImpl implements IAuditService {
    private static final String MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}";

    private static final String EXCEPTION_MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     * 格式为：{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作信息}
     * 例子：2020-02-04 09:13:34.650|user-center|com.central.user.controller.SysUserController|saveOrUpdate|1|admin|webApp|新增用户:admin
     */
    @Override
    public void save(Audit audit) {
        log.debug(MSG_PATTERN
                , audit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                , audit.getApplicationName(), audit.getClassName(), audit.getMethodName()
                , audit.getUserId(), audit.getUserName(), audit.getCompanyId()
                , audit.getOperation());
    }

    /**
     * 保存异常日志
     *格式为：{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作信息}|{异常信息}
     * @param exceptionAudit :
     * @return void
     * @author yw
     * @date 2021/2/2
     */
    @Override
    public void save(ExceptionAudit exceptionAudit) {
        log.debug(EXCEPTION_MSG_PATTERN, exceptionAudit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
                , exceptionAudit.getExApplicationName(), exceptionAudit.getExClassName(), exceptionAudit.getExMethodName()
                , exceptionAudit.getUserId(), exceptionAudit.getUserName(), exceptionAudit.getCompanyId()
                , exceptionAudit.getOperation(),exceptionAudit.getExceptionMsg());
    }
}
