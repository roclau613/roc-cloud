package com.roc.cloud.log.service;

import com.roc.cloud.log.model.Audit;
import com.roc.cloud.log.model.ExceptionAudit;

/**
 * 审计日志接口
 *
 * @author: Roc
 * @date 2020/2/3
 * <p>
 */
public interface IAuditService {

    /**
     * 保存审计日志
     *
     * @param audit :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    void save(Audit audit);

    /**
     * 保存异常日志
     * @param exceptionAudit :
     *
     * @return void
     * @author yw
     * @date 2021/2/2
     */
    void save(ExceptionAudit exceptionAudit);
}
