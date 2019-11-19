package com.demo.weekreport;

import com.demo.common.model.Blog;
import com.demo.common.model.TbWeekReport;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogValidator.
 */
public class WeekReportValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("tbWeekReport.summary", "summaryMsg", "请输入 总结！");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(TbWeekReport.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/weekreport/save"))
			controller.render("/weekreport/add.html");
		else if (actionKey.equals("/weekreport/update"))
			controller.render("/weekreport/edit.html");
	}
}
