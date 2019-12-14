package com.demo.search;

import lombok.Data;

import java.util.List;

/**
 * 自定义分页实体类
 */
@Data
public class ResultModel<T> {

    // 商品列表
    private List<T> list;
    // 商品总数
    private Long totalRow;
    // 总页数
    private Long totalPage;
    // 当前页
    private long pageNumber;
    private long pageSize;

}
