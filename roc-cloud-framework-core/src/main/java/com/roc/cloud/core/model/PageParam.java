package com.roc.cloud.core.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 全局统一分页查询参数
 * </p>
 *
 * @author zj
 * @version 1.0
 * @date 2020-09-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam implements Serializable {

    /**
     * 页码
     * ccc
     *
     * @author Roc
     * @date 2020/10/22
     **/
    @ApiModelProperty("页码")
    private int pageNum = 1;

    /**
     * 每页条数
     *
     * @return
     * @author Roc
     * @date 2020/10/22
     **/
    @ApiModelProperty("每页条数")
    private int pageSize = 10;
    /**
     * 方便能不返回总条数尽量不返回，提高查询效率，节约服务器资源
     *
     * @author Jewei
     * @date 2022/03/11
     */
    @ApiModelProperty("是否返回总条数，默认返回，能不返回总条数尽量传false，提高查询效率，节约服务器资源")
    private boolean count = true;

    /**
     * 常用构造
     *
     * @param pageNum  :
     * @param pageSize :
     * @return * @return : null
     * @author Jewei
     * @date 2022/3/11 10:43
     **/
    public PageParam(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
