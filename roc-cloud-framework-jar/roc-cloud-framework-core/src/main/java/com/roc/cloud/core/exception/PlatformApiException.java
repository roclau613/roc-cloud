package com.roc.cloud.core.exception;

import com.roc.cloud.core.constant.LogLevelConstant;
import com.roc.cloud.core.model.CodeEnum;
import lombok.Data;

/**
 * 平台服务统一对外接口异常
 *
 * @author: Roc
 */
@Data
public class PlatformApiException extends RuntimeException {

    private static final long serialVersionUID = 6610083281801529147L;

    private Integer errCode;

    private String errMsg;

    private Integer errLevel;

    /**
     * PlatformApiException
     *
     * @param errorCodeObj :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public PlatformApiException(CodeEnum errorCodeObj) {
        super(errorCodeObj.getMessage());
        this.errCode = errorCodeObj.getCode();
        this.errMsg = errorCodeObj.getMessage();
        this.errLevel = LogLevelConstant.ERROR;
    }

    /**
     * PlatformApiException
     *
     * @param errorCodeObj :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public PlatformApiException(CodeEnum errorCodeObj, Integer errLevel) {
        super(errorCodeObj.getMessage());
        this.errCode = errorCodeObj.getCode();
        this.errMsg = errorCodeObj.getMessage();
        this.errLevel = errLevel;
    }

    /**
     * PlatformApiException
     *
     * @param errorCode :
     * @param errorMsg  :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public PlatformApiException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errCode = errorCode;
        this.errMsg = errorMsg;
        this.errLevel = LogLevelConstant.ERROR;
    }

    /**
     * PlatformApiException
     *
     * @param errorCode :
     * @param errorMsg  :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public PlatformApiException(Integer errorCode, String errorMsg, Integer errLevel) {
        super(errorMsg);
        this.errCode = errorCode;
        this.errMsg = errorMsg;
        this.errLevel = errLevel;
    }

    /**
     * PlatformApiException
     *
     * @param errMsg :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public PlatformApiException(String errMsg) {
        super(errMsg);
        this.errCode = CodeEnum.DEFAULT_ERROR.getCode();
        this.errMsg = errMsg;
        this.errLevel = LogLevelConstant.ERROR;
    }

    /**
     * PlatformApiException
     *
     * @param errMsg :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public PlatformApiException(String errMsg, Integer errLevel) {
        super(errMsg);
        this.errCode = CodeEnum.DEFAULT_ERROR.getCode();
        this.errMsg = errMsg;
        this.errLevel = errLevel;
    }

}
