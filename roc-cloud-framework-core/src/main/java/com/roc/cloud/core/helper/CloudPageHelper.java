package com.roc.cloud.core.helper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.roc.cloud.common.model.PageParam;
import com.roc.cloud.common.model.PageResult;

/**
 * 统一分页处理逻辑
 *
 * @date: 2020/9/30 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class CloudPageHelper extends PageHelper {

    protected static ThreadLocal<Boolean> PAGE_LOCAL_TYPE = new ThreadLocal<>();

    public static Boolean getLocalPageType() {
        return PAGE_LOCAL_TYPE.get();
    }

    public static void clearLocalPageType() {
        PAGE_LOCAL_TYPE.remove();
    }

    protected static void setLocalPageType(Boolean type) {
        PAGE_LOCAL_TYPE.set(type);
    }

    /**
     * 开始调用分页插件-单个值
     *
     * @param pageNum : 页码
     * @param pageSize : 页数量
     * @return com.github.pagehelper.Page<E>
     * @author Roc
     * @date 2020/9/30
     **/
    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        setLocalPageType(Boolean.TRUE);
        return PageMethod.startPage(pageNum, pageSize, true);
    }

    /**
     * 开始调用分页插件
     *
     * @param pageParam : 参数对象
     * @return com.github.pagehelper.Page<E>
     * @author Roc
     * @date 2020/9/30
     **/
    public static <E> Page<E> startPage(PageParam pageParam) {
        setLocalPageType(Boolean.TRUE);
        return PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize(), pageParam.isCount());
    }

    /**
     * 获取分页详细对象
     *
     * @param result :
     * @return com.github.pagehelper.PageInfo
     * @author Roc
     * @date 2020/10/23
     **/
    public static PageInfo getPageInfo(Object result) {
        return new PageInfo((Page) result);
    }

    /**
     * 获取分页简单对象
     *
     * @param result :
     * @return com.github.pagehelper.PageInfo
     * @author Roc
     * @date 2020/10/23
     **/
    public static PageInfo getPageResult(Object result) {
        PageInfo pageInfo = getPageInfo(result);
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setTotalCount((int) pageInfo.getTotal());
        pageResult.setTotalPage(pageInfo.getPages());
        return new PageInfo((Page) result);
    }
}
