package com.demo.project;

import com.demo.blog.BlogInterceptor;
import com.demo.blog.BlogValidator;
import com.demo.common.model.Blog;
import com.demo.common.model.TbProject;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * ProjectController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class ProjectController extends Controller {

	private String modelKey = "project";
	
	@Inject
	ProjectService service;
	
	public void index() {
		setAttr(modelKey+"Page", service.paginate(getParaToInt(0, 1), 10));
		render(modelKey+".html");
	}
	
	public void add() {
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void save() {
		getBean(TbProject.class).save();
		redirect("/"+modelKey);
	}
	
	public void edit() {
		setAttr("tbProject", service.findById(getParaToInt()));
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void update() {
		TbProject bean = getBean(TbProject.class);
		bean.update();
		redirect("/"+modelKey);
	}
	
	public void delete() {
		service.deleteById(getParaToInt());
		redirect("/"+modelKey);
	}
}


