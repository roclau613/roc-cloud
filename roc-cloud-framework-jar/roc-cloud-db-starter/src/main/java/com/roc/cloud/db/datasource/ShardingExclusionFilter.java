package com.roc.cloud.db.datasource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

/**
 * @className: ShardingExclusionFilter
 * @description: sharding启动过滤
 * @author: Roc
 * @date: 2020/9/28
 **/
public class ShardingExclusionFilter implements AutoConfigurationImportFilter {

    private static final Set<String> SHOULD_SKIP = new HashSet<>(
            Arrays.asList("org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration",
                    "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure"));

    @Override
    public boolean[] match(String[] classNames, AutoConfigurationMetadata metadata) {
        boolean[] matches = new boolean[classNames.length];

        for (int i = 0; i < classNames.length; i++) {
            matches[i] = !SHOULD_SKIP.contains(classNames[i]);
        }
        return matches;
    }

}
