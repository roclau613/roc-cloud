package com.roc.cloud.core.model;

/**
 * @description: 常用枚举：是否 true false
 * @date: 2021/5/11 9:36
 * @author: Jewei
 * @version: 1.0
 */
public enum BoolIntegerEnum {

    /**
     * 是 true
     */
    FALSE(0, "否"),

    /**
     * 否 false
     */
    TRUE(1, "是"),
    ;
    /**
     * code
     */
    Integer code;
    /**
     * 描述
     */
    String desc;

    /**
     * @param code
     * @param desc
     */
    BoolIntegerEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @param code
     * @return
     */
    public static BoolIntegerEnum getEnumByCode(Integer code) {
        for (BoolIntegerEnum enu : BoolIntegerEnum.values()) {
            if (enu.getCode().equals(code)) {
                return enu;
            }
        }
        return null;
    }

    /**
     * @param desc
     * @return
     */
    public static BoolIntegerEnum getEnumByDesc(String desc) {
        for (BoolIntegerEnum enu : BoolIntegerEnum.values()) {
            if (enu.getDesc().equals(desc)) {
                return enu;
            }
        }
        return null;
    }
}
