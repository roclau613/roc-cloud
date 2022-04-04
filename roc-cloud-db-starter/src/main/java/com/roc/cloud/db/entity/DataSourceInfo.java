package com.roc.cloud.db.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @ClassName: DataSourceInfo
 * @Description: 数据源信息
 * @author: Roc
 * @date: 2020/9/24 15:45
 **/
@Data
public class DataSourceInfo implements Serializable {

    private static final long serialVersionUID = -1205937976452078853L;
    /**
     * 数据连接地址
     */
    private String url;
    /**
     * 连接驱动，默认mysql数据源
     */
    private String driver = DataSourceDriverEnum.MYSQL_SERVER.getDriver();
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
