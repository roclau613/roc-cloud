package com.roc.cloud.core.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 熟悉复制
 *
 * @author yw
 * @return
 * @date 2020/10/16
 */
@Slf4j
public class BeanCopyUtils {

    /**
     * 将源POJO对象数据复制给目标POJO对象的同名属性
     *
     * @param source 源
     * @param target 目标
     * @param <S>
     * @param <T>
     * @return 目标
     * @date 2018年12月4日
     * @version 1.0
     */
    public static <S, T> T copy(S source, T target) {
        if (null == source || null == target) {
            return null;
        }
        BeanUtil.copyProperties(source, target);
        return target;
    }

    /**
     * 将源POJO对象数据复制给目标POJO对象的同名属性
     *
     * @param source :
     * @param clazz :
     * @return T
     * @author Roc
     * @date 2020/11/3
     **/
    public static <T, S> T copy(S source, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t = clazz.newInstance();
        return copy(source, t);
    }


    /**
     * 将源POJO对象数据复制给目标POJO对象的同名属性
     *
     * @param source :
     * @param clazz :
     * @return T
     * @author Roc
     * @date 2020/11/3
     **/
    public static <T, S> T copy(S source, Class<T> clazz, String... ignoreProperties) throws IllegalAccessException, InstantiationException {
        T t = clazz.newInstance();
        return copy(source, t, ignoreProperties);
    }


    /**
     * 将源POJO对象数据复制给目标POJO对象的同名属性
     *
     * @param source 源
     * @param target 目标
     * @param ignoreProperties 无需转换的属性
     * @param <S>
     * @param <T>
     * @return 目标
     * @date 2019-1-29
     */
    public static <S, T> T copy(S source, T target, String... ignoreProperties) {
        if (null == source || null == target) {
            return null;
        }
        BeanUtil.copyProperties(source, target, ignoreProperties);
        return target;
    }


    /**
     * 将源POJO对象数据复制给目标POJO对象的同名属性
     *
     * @param source 源
     * @param target 目标
     * @param ignoreNullProperties 是否无视null值字段（如果为true则无视）
     * @param <S>
     * @param <T>
     * @return 目标
     * @date 2019-1-29
     */
    public static <S, T> T copy(S source, T target, boolean ignoreNullProperties) {
        if (null == source || null == target) {
            return null;
        }
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(ignoreNullProperties));
        return target;
    }

    /**
     * 对象List复制
     *
     * @param ss
     * @param cls
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> ss, Class<T> cls) {
        if (CollectionUtil.isEmpty(ss)) {
            return new ArrayList<>();
        }
        List<T> tRes = new ArrayList<>();
        try {
            for (S s : ss) {
                T t = cls.newInstance();
                BeanUtil.copyProperties(s, t);
                tRes.add(t);
            }
        } catch (Exception e) {
            log.info("类型转换异常，异常信息: ", e);
        }

        return tRes;
    }


    /**
     * 对象List复制
     *
     * @param ss
     * @param cls
     * @param ignoreProperties
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> ss, Class<T> cls, String... ignoreProperties) {
        if (CollectionUtil.isEmpty(ss)) {
            return new ArrayList<>();
        }
        List<T> tRes = new ArrayList<>();
        try {
            for (S s : ss) {
                T t = cls.newInstance();
                BeanUtil.copyProperties(s, t, ignoreProperties);
                tRes.add(t);
            }
        } catch (Exception e) {
            log.info("类型转换异常，异常信息: ", e);
        }

        return tRes;
    }

    /**
     * 对象List复制
     *
     * @param ss
     * @param cls
     * @param ignoreNullProperties
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> ss, Class<T> cls, boolean ignoreNullProperties) {
        if (CollectionUtil.isEmpty(ss)) {
            return new ArrayList<>();
        }
        List<T> tRes = new ArrayList<>();
        try {
            for (S s : ss) {
                T t = cls.newInstance();
                BeanUtil.copyProperties(s, t, CopyOptions.create().setIgnoreNullValue(ignoreNullProperties));
                tRes.add(t);
            }
        } catch (Exception e) {
            log.info("类型转换异常，异常信息: ", e);
        }

        return tRes;
    }


    /**
     * 序列化为byte[]
     *
     * @param object :
     * @return byte[]
     * @author liupeng
     * @date 2020/5/27
     **/
    public static byte[] serialize(Object object) {
        if (null == object) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("Serialization failed: ", e);
            return null;
        } finally {
            IoUtil.close(oos);
            IoUtil.close(bos);
        }
    }

    /**
     * 反序列化为Object
     *
     * @param bytes :
     * @return java.lang.Object
     * @author liupeng
     * @date 2020/5/27
     **/
    public static <T> T deserialize(byte[] bytes, Class<T> classType) {
        if (null == bytes) {
            return null;
        }
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Bytes Could not deserialize: ", e);
            return null;
        } finally {
            IoUtil.close(bais);
        }
    }


    /**
     * 深度拷贝List
     *
     * @param src :
     * @return List<T>
     * @author liupeng
     * @date 2020/5/27
     **/
    public static <T> List<T> deepCopyList(List<T> src) {
        if (CollectionUtil.isEmpty(src)) {
            return new ArrayList<>();
        }

        ObjectOutputStream out = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Bytes Could not deserialize: ", e);
            return null;
        } finally {
            IoUtil.close(out);
        }

    }

}
