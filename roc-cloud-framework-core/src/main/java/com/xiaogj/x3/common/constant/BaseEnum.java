package com.roc.cloud.common.constant;

/**
 * <p>
 * 枚举基类
 * </p>
 *
 * @author Roc
 * @description:
 * @data 2020-11-03
 * @version: 1.0
 */
public interface BaseEnum<E extends BaseEnum> {

    /**
     * 建议选国际化的key
     *
     * @return java.lang.String
     * @author Roc
     * @date 2020/11/9
     **/
    String getKey();

    /**
     * 建议存数据库
     *
     * @return int
     * @author Roc
     * @date 2020/11/9
     **/
    int getValue();

    /**
     * 描述
     *
     * @return String
     * @author Roc
     * @date 2020/11/9
     **/
    String getDescription();

}
