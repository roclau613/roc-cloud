package com.roc.cloud.core.model;


/**
 * <p>
 * 统一全局错误类
 * </p>
 *
 * @author zj
 * @version 1.0
 * @date 2020-09-28
 */
public enum CodeEnum {
    /**
     * 当前页面太火爆，请稍后再来
     */
    ERROR(500, "当前页面太火爆，请稍后再来"),

    /**
     * 远程服务错误
     */
    REMOTE_SERVICE_ERROR(501, "远程服务错误"),
    /**
     * 远程服务未找到
     */
    REMOTE_SERVICE_NOT_FOUND(404, "远程服务未找到"),
    /**
     * 字段说明
     */
    SUCCESS(200, "成功"),
    /**
     * 参数错误
     */
    BIND_EXCEPTION(400, "参数错误"),
    /**
     * 默认错误码
     */

    DEFAULT_ERROR(60000, "默认错误码"),

    ;

    private Integer code;

    private String message;

    /**
     * CodeEnum
     *
     * @param code    :
     * @param message :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * getCode
     *
     * @return java.lang.Integer
     * @author wuzhichang
     * @date 2020/11/19
     **/

    public Integer getCode() {
        return code;
    }

    /**
     * setCode
     *
     * @param code :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * getMessage
     *
     * @return java.lang.String
     * @author Roc
     * @date 2020/9/30
     **/
    public String getMessage() {
        return message;
    }

    /**
     * setMessage
     *
     * @param message :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    public void setMessage(String message) {
        this.message = message;
    }
}
