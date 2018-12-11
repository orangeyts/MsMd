package com.demo.env;

import com.demo.common.model.TbEnvConfig;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * StepController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class TbEnvConfigController extends Controller {

	private String modelKey = "env";
	
	@Inject
	TbEnvConfigService service;
	
	public void index() {
        Page<TbEnvConfig> paginate = service.paginate(getParaToInt(0, 1), 10, 0);
        setAttr(modelKey+"Page", paginate);
		render(modelKey+".html");
	}
	
	public void add() {
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void save() {
		getBean(TbEnvConfig.class).save();
		redirect("/"+modelKey);
	}
	
	public void edit() {
		setAttr("tbEnvConfig", service.findById(getParaToInt()));
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void update() {
		TbEnvConfig bean = getBean(TbEnvConfig.class);
		bean.update();
		redirect("/"+modelKey);
	}
	
	public void delete() {
		service.deleteById(getParaToInt());
		redirect("/"+modelKey);
	}
}


