package com.roc.cloud.log.service.impl;

import com.roc.cloud.log.model.Audit;
import com.roc.cloud.log.model.ExceptionAudit;
import com.roc.cloud.log.service.IAuditService;
import com.roc.cloud.log.utils.LogSyncUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;

/**
 * 审计日志实现类-es
 *
 * @author: Roc
 * @date 2020/2/8
 * <p>
 */
@Slf4j
@ConditionalOnProperty(name = "roc.cloud.audit-log.log-type", havingValue = "es")
public class EsAuditServiceImpl implements IAuditService {


    @Async
    @Override
    public void save(Audit audit) {
        //send message
        LogSyncUtils.sendMq(audit);
    }

    /**
     * 保存异常日志
     *
     * @param exceptionAudit :
     * @return void
     * @author yw
     * @date 2021/2/2
     */
    @Override
    public void save(ExceptionAudit exceptionAudit) {
        LogSyncUtils.sendExceptionMq(exceptionAudit);
    }
}
