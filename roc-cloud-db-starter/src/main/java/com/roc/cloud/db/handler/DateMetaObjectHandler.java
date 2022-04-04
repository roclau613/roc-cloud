package com.roc.cloud.db.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.roc.cloud.db.properties.MybatisPlusAutoFillProperties;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自定义填充公共字段
 *
 * @author: Roc
 * @date 2018/12/11
 * <p>
 */
public class DateMetaObjectHandler implements MetaObjectHandler {
    private MybatisPlusAutoFillProperties autoFillProperties;

    public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(), metaObject);
        Object deleted = getFieldValByName(autoFillProperties.getDeletedField(), metaObject);
        if (createTime == null) {
            setFieldValByName(autoFillProperties.getCreateTimeField(), getNow(getFieldTypeByName(autoFillProperties.getCreateTimeField(), metaObject)), metaObject);
        }
        if (updateTime == null) {
            setFieldValByName(autoFillProperties.getUpdateTimeField(), getNow(getFieldTypeByName(autoFillProperties.getUpdateTimeField(), metaObject)), metaObject);
        }
        if (deleted == null) {
            setFieldValByName(autoFillProperties.getDeletedField(), false, metaObject);

        }
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(),getNow(getFieldTypeByName(autoFillProperties.getUpdateTimeField(), metaObject)), metaObject);
    }

    /**
     * 获取MetaObject字段类型
     * @param fieldName :
     * @param metaObject :
     * @return  * @return : java.lang.Class<?>
     * @author Jewei
     * @date 2021/2/2 10:37
     **/
    private Class<?> getFieldTypeByName(String fieldName, MetaObject metaObject){
        return metaObject.hasGetter(fieldName) ? metaObject.getGetterType(fieldName) : null;
    }
    /**
     *根据类型获取当前时间
     * @param cls :
     * @return  * @return : java.lang.Object
     * @author Jewei
     * @date 2021/2/2 10:21
     **/
    private Object getNow(Class<?> cls){
        if(cls==null){
            return null;
        }
        if(cls.equals(LocalDateTime.class)){
            return LocalDateTime.now();
        }
        else {
            return new Date();
        }
    }
}