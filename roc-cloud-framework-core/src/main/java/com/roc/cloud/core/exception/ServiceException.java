package com.roc.cloud.core.exception;

import com.roc.cloud.common.model.CodeEnum;
import lombok.Data;

/**
 * 统一业务异常
 *
 * @author: Roc
 */
@Data
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 6610083281801529147L;

    private Integer errCode;

    private String errMsg;

    /**
     * ServiceException
     *
     * @param errorCodeObj :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public ServiceException(CodeEnum errorCodeObj) {
        super(errorCodeObj.getMessage());
        this.errCode = errorCodeObj.getCode();
        this.errMsg = errorCodeObj.getMessage();
    }

    /**
     * ServiceException
     *
     * @param errorCode :
     * @param errorMsg  :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public ServiceException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errCode = errorCode;
        this.errMsg = errorMsg;
    }

    /**
     * ServiceException
     *
     * @param errMsg :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public ServiceException(String errMsg) {
        super(errMsg);
        this.errCode = CodeEnum.DEFAULT_ERROR.getCode();
        this.errMsg = errMsg;
    }

}
