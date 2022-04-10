package com.roc.cloud.core.model;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回值格式
 *
 * @author Roc
 * @version 1.0, 2020/5/5 15:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private T data;
    private Integer code;
    private String msg;
    private PageResult page;
    private Long currentTimeMillis = System.currentTimeMillis();

    public static <T> Result<T> succeed(String msg) {
        return of(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed() {
        return of(null, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage());
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, CodeEnum.SUCCESS.getCode(), "");
    }

    public static <T> Result<T> succeed(T model, PageResult page) {
        return of(model, CodeEnum.SUCCESS.getCode(), "", page);
    }

    public static <T> Result<T> succeed(T model, PageInfo pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setTotalCount((int) pageInfo.getTotal());
        pageResult.setTotalPage(pageInfo.getPages());
        return of(model, CodeEnum.SUCCESS.getCode(), "", pageResult);
    }

    public static <T> Result<T> succeed(T model, PageParam page) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page.getPageNum());
        pageResult.setTotalCount(0);
        pageResult.setTotalPage(0);
        return of(model, CodeEnum.SUCCESS.getCode(), "", pageResult);
    }

    public static <T> Result<T> of(T datas, Integer code) {
        return of(datas, code, null, null);
    }

    public static <T> Result<T> of(T datas, Integer code, String msg) {
        return of(datas, code, msg, null);
    }

    public static <T> Result<T> of(Integer code, String msg) {
        return of(null, code, msg, null);
    }

    public static <T> Result<T> of(T datas, Integer code, String msg, PageResult page) {
        return new Result<>(datas, code, msg, page, System.currentTimeMillis());
    }

    public static <T> Result<T> failed(String msg) {
        return of(null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed() {
        return of(null, CodeEnum.ERROR.getCode(), CodeEnum.ERROR.getMessage());
    }

    public static <T> Result<T> failed(T model, String msg) {
        return of(model, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(CodeEnum codeEnum, String msg) {
        return of(null, codeEnum.getCode(), msg);
    }

    public static <T> Result<T> failed(Integer errorCode, String msg) {
        return of(null, errorCode, msg);
    }

}
