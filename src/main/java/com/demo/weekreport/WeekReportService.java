package com.demo.weekreport;

import com.demo.common.model.TbUser;
import com.demo.common.model.TbWeekReport;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class WeekReportService {
	
	/**
	 * 所有的 dao 对象也放在 Service 中，并且声明为 private，避免 sql 满天飞
	 * sql 只放在业务层，或者放在外部 sql 模板，用模板引擎管理：
	 * 			http://www.jfinal.com/doc/5-13
	 */
	private TbWeekReport dao = new TbWeekReport().dao();
	
	public Page<TbWeekReport> paginate(int pageNumber, int pageSize, String condition,List<Object> paramValue) {
		return dao.paginate(pageNumber, pageSize, "select * ",
				"FROM tb_week_report WHERE 1=1 " + condition + " order by createTime desc", paramValue.toArray());
	}
	
	public TbWeekReport findById(int id) {
		return dao.findById(id);
	}
	
	public void deleteById(int id) {
		dao.deleteById(id);
	}
}
