package com.demo.search;


/**
 *
 */
public interface SearchService {

    public ResultModel query(String queryString, String price, Integer page) throws Exception;

    /**
     * 创建全量索引
     */
    public void createFullIndex()throws Exception;
    public void delTermIndex(String id)throws Exception;
}
