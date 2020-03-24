package com.demo.api;

import com.demo.common.model.TbTopology;
import com.demo.common.model.TbTopology;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * TbStepService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class TopologyService {
	
	/**
	 * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
	 * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
	 * 			http://www.jfinal.com/doc/5-13
	 */
	private TbTopology dao = new TbTopology().dao();
	
	public Page<TbTopology> paginate(int pageNumber, int pageSize) {
		Page<TbTopology> paginate = dao.paginate(pageNumber, pageSize, "select *", "from tb_topology order by id asc");
		return paginate;
	}
	
	public TbTopology findById(int id) {
		return dao.findById(id);
	}
	
	public void deleteById(int id) {
		dao.deleteById(id);
	}

	public void checkRepeatName(TbTopology bean) {
		String sql = "select id from tb_topology where 1=1 and title=?";
		if (bean.getId() != null){
			sql = sql + " AND id !="+bean.getId();
		}
		List<TbTopology> TbTopologys = dao.find(sql,bean.getName());
		if (TbTopologys.size() > 0){
			throw new RuntimeException("名字重复");
		}
	}

    public List<TbTopology> getList() {
		return dao.findAll();
    }
}
