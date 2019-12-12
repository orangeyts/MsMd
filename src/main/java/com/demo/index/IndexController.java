package com.demo.index;

import com.alibaba.druid.util.StringUtils;
import com.demo.common.model.Blog;
import com.demo.common.model.TbUser;
import com.demo.constant.ConstantConfig;
import com.demo.tbuser.TbUserService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * IndexController
 */
@Slf4j
public class IndexController extends Controller {

	@Inject
	TbUserService service;
	public void index() {
		render("index.html");
	}
	public void toLogin() {
		String url = getPara("url");
		setAttr("url",url);
		render("login.html");
	}
	public void logout() {
		getSession().invalidate();
		render("login.html");
	}
	public void login() throws UnsupportedEncodingException {
		String username = getPara("username");
		String password = getPara("password");
		String url = getPara("url");
		TbUser login = service.login(username, password);
		if (login != null){
			getSession().setAttribute(ConstantConfig.SESSION_KEY,login);
			if (StringUtils.isEmpty(url)){
				forwardAction("/weekreport");
			}else {
				String decodeUrl = URLDecoder.decode(url, "UTF-8");
				log.info("decodeUrl: [{}]",decodeUrl);
				redirect(decodeUrl);
			}
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



