package com.roc.cloud.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @program: simple_tools
 * @description: 精准浮点运算
 * @author: Roc
 * @create: 2019-10-18 11:51
 **/
public class AmountUtils {

    /**
     * 金额格式化，并保留2位小数，不足2位小数补0
     *
     * @param obj 传入的小数
     * @return
     * @desc 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。
     * 2.传入的参数等于0，则直接返回字符串"0.00"
     * 3.大于1的小数，直接格式化返回字符串
     */
    public static String amountFormat(BigDecimal obj) {
        if (obj == null) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##,###.00");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj);
        } else if (obj.compareTo(BigDecimal.ZERO) < 0 && obj.compareTo(new BigDecimal(-1)) > 0) {
            df = new DecimalFormat("0.00");
            return df.format(obj);
        } else {
            return df.format(obj);
        }
    }
}

