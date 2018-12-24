package com.demo.index;

import com.demo.constant.ConstantConfig;
import com.jfinal.core.Controller;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * IndexController
 */
public class IndexController extends Controller {
	public void index() {
		render("index.html");
	}
	public void toLogin() {
		render("login.html");
	}
	public void login() {
		String username = getPara("username");
		String password = getPara("password");
		if ("u".equals(username) && "p".equals(password)){
			getSession().setAttribute(ConstantConfig.SESSION_KEY,username);
			render("index.html");
		}else {
			renderHtml("登录错误");
		}
	}
}



