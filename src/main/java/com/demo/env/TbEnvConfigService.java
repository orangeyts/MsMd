package com.demo.env;

import com.demo.common.model.TbEnvConfig;
import com.jfinal.plugin.activerecord.Page;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * TbStepService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class TbEnvConfigService {
	
	/**
	 * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
	 * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
	 * 			http://www.jfinal.com/doc/5-13
	 */
	private TbEnvConfig dao = new TbEnvConfig().dao();
	
	public Page<TbEnvConfig> paginate(int pageNumber, int pageSize,int projectId) {
		return dao.paginate(pageNumber, pageSize, "select *", "from tb_env_config where 1=1 order by id asc");
	}
	
	public TbEnvConfig findById(int id) {
		return dao.findById(id);
	}
	
	public void deleteById(int id) {
		dao.deleteById(id);
	}
}
