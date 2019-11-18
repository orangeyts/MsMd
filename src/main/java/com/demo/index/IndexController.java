package com.demo.index;

import com.demo.common.model.Blog;
import com.demo.common.model.TbUser;
import com.demo.constant.ConstantConfig;
import com.demo.tbuser.TbUserService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * IndexController
 */
public class IndexController extends Controller {

	@Inject
	TbUserService service;
	public void index() {
		render("index.html");
	}
	public void toLogin() {
		render("login.html");
	}
	public void logout() {
		getSession().invalidate();
		render("login.html");
	}
	public void login() {
		String username = getPara("username");
		String password = getPara("password");
		TbUser login = service.login(username, password);
		if (login != null){
			getSession().setAttribute(ConstantConfig.SESSION_KEY,login);
			forwardAction("/weekreport");
		}else {
			renderHtml("登录错误");
		}
	}


	/**
	 * 到注册页面
	 */
	public void toRegister() {
		render("/users/add.html");
	}
	/**
	 * 注册
	 */
	public void register() {
		TbUser bean = getBean(TbUser.class);
		if(service.checkLoginName(bean)){
			bean.save();
		}else {
			renderText("用户已存在");
		}
		redirect("/weekreport/index");
	}
}



