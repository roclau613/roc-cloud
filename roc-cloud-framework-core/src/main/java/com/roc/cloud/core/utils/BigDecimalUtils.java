package com.roc.cloud.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @program: simple_tools
 * @description: 精准浮点运算
 * @author: Roc
 * @create: 2019-10-18 11:51
 **/
public class BigDecimalUtils {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 这个类不能实例化
     */
    private BigDecimalUtils() {
    }

    /**
     * 是否大于零
     *
     * @param param
     * @return
     */
    public static boolean isGreaterZero(BigDecimal param) {
        return Objects.nonNull(param) && param.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 是否为null或者等于零
     *
     * @param param
     * @return
     */
    public static boolean isNullOrZero(BigDecimal param) {
        return Objects.isNull(param) || param.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 是否小于零
     *
     * @param param
     * @return
     */
    public static boolean isLessZero(BigDecimal param) {
        return Objects.nonNull(param) && param.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 参数1加上参数2
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Roc
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 参数1加上参数2
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     * @author Roc
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    /**
     * 参数1减去参数2
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @author Roc
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 参数1减去参数2
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     * @author Roc
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * 参数1乘以参数2
     *
     * @param b1 被除数
     * @param b2 除数
     * @return 两个参数的商
     * @author Roc
     */
    public static BigDecimal mul(BigDecimal b1, BigDecimal b2) {
        if (BigDecimal.ZERO.compareTo(b1) == 0 || BigDecimal.ZERO.compareTo(b2) == 0) {
            return BigDecimal.ZERO;
        }
        return b1.multiply(b2);
    }

    /**
     * 参数1乘以参数2，默认保留2位
     *
     * @param b1 被除数
     * @param b2 除数
     * @return 两个参数的商
     * @author Roc
     */
    public static BigDecimal defaultMul(BigDecimal b1, BigDecimal b2) {
        return mul(b1, b2, 2);
    }

    /**
     * 参数1乘以参数2，默认保留指定位
     *
     * @param b1 被除数
     * @param b2 除数
     * @return 两个参数的商
     * @author Roc
     */
    public static BigDecimal mul(BigDecimal b1, BigDecimal b2, int scale) {
        return mul(b1, b2).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 参数1乘以参数2
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     * @author Roc
     */
    public static double mul(double v1, double v2) {
        return mul(BigDecimal.valueOf(v1), BigDecimal.valueOf(v2)).doubleValue();
    }

    /**
     * 参数1除以参数2，默认保留指定位
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @author Roc
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        if (v1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return v1.divide(v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 参数1除以参数2，默认保留8位
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @author Roc
     */
    public static BigDecimal defaultDiv(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, 8);
    }

    /**
     * 参数1除以参数2，默认保留指定位
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @author Roc
     */
    public static double div(double v1, double v2, int scale) {
        return div(new BigDecimal(v1), new BigDecimal(v1), scale).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     * @author Roc
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = BigDecimal.valueOf(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }



}

