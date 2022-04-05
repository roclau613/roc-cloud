package com.roc.cloud.db.entity;

/**
 * @ClassName: DataSourceDriverEnum
 * @Description: 连接驱动枚举
 * @author: Roc
 * @date: 2020/9/24 14:54
 **/
public enum DataSourceDriverEnum {

    /**
     * SQL server
     */
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    /**
     * msyql server 8
     */
    MYSQL_SERVER("com.mysql.cj.jdbc.Driver"),
    /**
     * oracle server
     */
    ORACLE_SERVER("oracle.jdbc.driver.OracleDriver");

    private String driver;

    DataSourceDriverEnum(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }
}
