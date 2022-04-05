package com.roc.cloud.core.model;


/**
 * 全局数据记录删除状态枚举类
 *
 * @author: xdli
 * @since: 1.0
 * @version: 1.0
 */
public enum DeletedEnum {
    NORMAL(0, "正常"),
    ERROR(1, "已删除");

    private Integer val;

    private String message;

    DeletedEnum(Integer val, String message) {
        this.val = val;
        this.message = message;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
