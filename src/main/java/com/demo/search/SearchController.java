package com.demo.search;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

/**
 * 周报-搜索
 */
public class SearchController extends Controller {

	@Inject
    SearchService searchService;
	
	public void index() throws Exception {
		String queryString = getPara("queryString");
		String price = getPara("price");
		Integer pageNumber = getParaToInt("page", 1);
		ResultModel query = searchService.query(queryString, price, pageNumber);
		setAttr("page", query);
		setAttr("startTime",queryString);
		setAttr("endTime",price);
		render("index.html");
	}
}


