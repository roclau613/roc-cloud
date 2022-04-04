package com.roc.cloud.core.model;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @author zj <br>
 * @version 1.0 <br>
 * @date 2020-10-19 <br>
 * @since 1.0 <br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo<T> implements Serializable {
    private PageResult page;
    private List<T> data;

    public PageInfo(Page<T> page){
        this.page = new PageResult();
        this.page.setPageNum(page.getPageNum());
        this.page.setTotalCount(Long.valueOf(page.getTotal()).intValue());
        this.page.setTotalPage(page.getPages());
        this.data = page.getResult();
    }

    public PageInfo(Integer pageNum, Integer totalPage, Integer totalCount,  List<T> data){
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageNum);
        pageResult.setTotalCount(totalCount);
        pageResult.setTotalPage(totalPage);
        this.page = pageResult;
        this.data = data;
    }
}
