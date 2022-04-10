package com.roc.cloud.core.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.Properties;


/**
 * mybatis-plus自动配置
 *
 * @author: Roc
 * @date 2020/4/5
 * <p>
 */
public class MybatisPlusAutoConfigure {


    /**
     * @param
     * @return com.github.pagehelper.PageInterceptor
     * @description: 分页查询配置
     * @version: 1.0
     * @date: 2020/8/13 10:22
     * @author: Roc
     */
    @Bean(name = "pageHelper")
    public PageInterceptor pageHelper() {
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        /**默认false，设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用*/
        properties.setProperty("offsetAsPageNum", "true");
        /**默认false，设置为true时，使用RowBounds分页会进行count查询 */
        properties.setProperty("rowBoundsWithCount", "true");
        /** 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 */
        properties.setProperty("reasonable", "true");
        /** always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page */
        properties.setProperty("returnPageInfo", "check");
        /** 支持通过Mapper接口参数来传递分页参数 */
        properties.setProperty("supportMethodsArguments", "false");
        /**  配置数据库的方言  */
        properties.setProperty("helperDialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
