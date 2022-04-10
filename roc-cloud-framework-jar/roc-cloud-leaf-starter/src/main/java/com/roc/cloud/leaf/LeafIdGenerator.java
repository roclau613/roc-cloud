package com.roc.cloud.leaf;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.roc.cloud.leaf.constants.Constant;
import com.roc.cloud.leaf.exception.ConfigException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * @className: LeafIdGenerator
 * @description: 非同一注册中心获取唯一ID
 * @Author: Roc
 * @date: 2020/9/22
 **/
@Slf4j
public class LeafIdGenerator {

    /**
     * 唯一id服务url
     **/
    private static String LEAF_BASE_URL;

    /**
     * 环境变量对象
     **/
    private Environment environment;

    /**
     * 获取配置
     *
     * @return void
     * @throws
     * @author liupeng
     * @date 2020/9/22
     **/
    public void readConfig() {
        LEAF_BASE_URL = environment.getProperty(Constant.CONFIG_LEAF_SEGMENT_HTTP_BASE_URL);
        if (StringUtils.isBlank(LEAF_BASE_URL)) {
            throw new ConfigException("not found config => roc.cloud.leaf.segment.http.base-url");
        }
    }

    /**
     * 构造bean
     *
     * @param environment :
     * @return
     * @throws
     * @author liupeng
     * @date 2020/9/22
     **/
    public LeafIdGenerator(Environment environment) {
        this.environment = environment;
        readConfig();
    }

    /**
     * http模式获取segmentId
     *
     * @param appName :
     * @return java.lang.String
     * @author liupeng
     * @date 2020/9/22
     **/
    public static String creatSegmentId(String appName) {
        try {
            HttpRequest request = HttpUtil.createGet(LEAF_BASE_URL + "/api/segment/get/" + appName);
            HttpResponse response = request.execute();
            if (response.isOk()) {
                return response.body();
            }
        } catch (Exception e) {
            log.error("creatSegmentId fail: ", e);
        }
        return Constant.LEAF_ZERO_ID;
    }

}
