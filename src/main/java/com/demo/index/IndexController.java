package com.demo.index;

import com.alibaba.druid.util.StringUtils;
import com.demo.common.model.Blog;
import com.demo.common.model.TbUser;
import com.demo.constant.ConstantConfig;
import com.demo.env.TbEnvConfigService;
import com.demo.tbuser.TbUserService;
import com.demo.util.MyBatisFormatter;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

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
	@Inject
	TbEnvConfigService tbEnvConfigService;

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
			Cookie cookie = new Cookie(ConstantConfig.SESSION_KEY,login.getId()+"");
			Map<String, String> config = tbEnvConfigService.getConfig();


			PropKit.use("a_little_config_db.txt");
			if (PropKit.getBoolean("devMode", false)){
				cookie.setDomain("localhost");
			}else {
				String cookiedomain = config.get(ConstantConfig.COOKIEDOMAIN);
				cookie.setDomain(cookiedomain);
			}
			cookie.setMaxAge(10000000);

			getResponse().addCookie(cookie);
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

	public void mybatisFormatSql() throws Exception {
		String sql = get("sql");
		log.info("sql: [{}]",sql);
		if (!StringUtils.isEmpty(sql)){
			sql = MyBatisFormatter.format(sql.trim());
		}
		setAttr("sql",sql);
		render("mybatisFormatSql.html");
	}

	public void mybatisFormatSqlAjax() throws Exception {
		String sql = get("sql");
		log.info("sql: [{}]",sql);
		if (!StringUtils.isEmpty(sql)){
			sql = MyBatisFormatter.format(sql.trim());
		}
		renderText(sql);
	}
}



