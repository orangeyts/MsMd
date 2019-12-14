package com.demo.search;

import java.util.List;

/**
 * 自定义分页实体类
 */
public class ResultModel<T> {

    // 商品列表
    private List<T> skuList;
    // 商品总数
    private Long recordCount;
    // 总页数
    private Long pageCount;
    // 当前页
    private long curPage;

    public List<T> getSkuList() {
        return skuList;
    }

    public void setList(List<T> skuList) {
        this.skuList = skuList;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }
}
