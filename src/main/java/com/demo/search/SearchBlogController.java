package com.demo.search;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexNotFoundException;

/**
 * 博客-搜索
 */
@Slf4j
public class SearchBlogController extends Controller {

	@Inject
	SearchBlogServiceImpl searchService;
	
	public void index() throws Exception {
		String category = getPara("category");
		String age = getPara("age");
		Integer pageNumber = getParaToInt("page", 1);
		ResultModel query = null;
		try {
			query = searchService.query(category, age, pageNumber);
		} catch (IndexNotFoundException e) {
			log.error("初次使用没有索引重新创建一个");
			searchService.createFullIndex();
		}
		setAttr("result", query);

		setAttr("page", query);
		setAttr("category",category);
		setAttr("age",age);
		render("index.html");
	}
	public void createFullIndex() throws Exception {
		searchService.createFullIndex();
		renderJson("success");
	}
}


