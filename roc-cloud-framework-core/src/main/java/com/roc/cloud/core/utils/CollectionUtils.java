package com.roc.cloud.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Sets;


/**
 * 集合交并集处理工具类  CollectionUtils
 * @date: 2020/11/8
 * @author: xdli
 * @since: 1.0
 * @version: 1.0
 */
public class CollectionUtils<T> {


    /**
     * 交集
     * @param source :
     * @param target :
     *
     * @return java.util.List<T>
     * @author xdli
     * @date 2020/11/8
     **/
    public List<T> and(List<T> source, List<T> target) {
        boolean sourceCondition = (source == null);
        boolean targetCondition = (target == null);
        if (sourceCondition && targetCondition) {
            return new ArrayList<>();
        }
        if (sourceCondition) {
            return target;
        }
        if (targetCondition) {
            return source;
        }
        return source.stream ().filter (target::contains).collect (Collectors.toList ());
    }



    /**
     * 两个set集合交集
     * @param set1 :
     * @param set2 :
     *
     * @return java.util.Set<java.lang.Long>
     * @author xdli
     * @date 2020/11/8
     **/
    public static Set<Long> join(Set<Long> set1, Set<Long> set2) {
        final Sets.SetView<Long> intersection = Sets.intersection(set1, set2);
        return intersection;
    }




    /**
     * 多个集合求交集
     * @param listArray :
     *
     * @return java.util.List<T>
     * @author xdli
     * @date 2020/11/8
     **/
    public List<T> theSameManyList(List<T>... listArray) {
        List<T> resultList = listArray[0];
        for (int i = 1; i < listArray.length; i++) {
            resultList = and (resultList, listArray[i]);
        }
        return resultList;
    }



    /**
     * 差集
     * @param mainList :
     * @param list2 :
     *
     * @return java.util.List<T>
     * @author xdli
     * @date 2020/11/8
     **/
    public List<T> andNot(List<T> mainList, List<T> list2) {
        return mainList.stream ().distinct ().filter (user -> {
            if (null == user) {return false;}
            return !list2.contains (user);
        }).collect (Collectors.toList ());
    }


    /**
     * 并集
     * @param unionList1 :
     * @param unionList2 :
     *
     * @return java.util.List<T>
     * @author xdli
     * @date 2020/11/8
     **/
    public List<T> unionList(List<T> unionList1, List<T> unionList2) {
        if (null == unionList2) {
            return unionList1;
        }
        unionList1.addAll (unionList2);
        return unionList1.parallelStream ().distinct ().collect (Collectors.toList ());
    }

}
