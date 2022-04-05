package com.roc.cloud.core.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页实体类
 *
 * @author: Roc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult implements Serializable {
    private static final long serialVersionUID = -275582248840137389L;
    /**
     * 页码
     **/
    private int pageNum;

    /**
     * 总页数
     **/
    private int totalPage;

    /**
     * 总记录数c
     **/
    private int totalCount;
}
