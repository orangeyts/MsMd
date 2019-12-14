package com.demo.search;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexNotFoundException;

/**
 * 周报-搜索
 */
@Slf4j
public class SearchController extends Controller {

	@Inject
	SearchServiceImpl searchService;
	
	public void index() throws Exception {
		String userName = getPara("userName");
		String summary = getPara("summary");
		Integer pageNumber = getParaToInt("page", 1);
		ResultModel query = null;
		try {
			query = searchService.query(summary, userName, pageNumber);
		} catch (IndexNotFoundException e) {
			log.error("初次使用没有索引重新创建一个");
			searchService.createFullIndex();
		}
		setAttr("result", query);

		setAttr("page", query);
		setAttr("userName",userName);
		setAttr("summary",summary);
		render("index.html");
	}
	public void createFullIndex() throws Exception {
		searchService.createFullIndex();
		renderJson("success");
	}
}


