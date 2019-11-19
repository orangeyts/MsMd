package com.demo.weekreport;

import com.alibaba.excel.EasyExcel;
import com.demo.blog.BlogInterceptor;
import com.demo.blog.BlogValidator;
import com.demo.common.model.Blog;
import com.demo.common.model.TbUser;
import com.demo.common.model.TbWeekReport;
import com.demo.tbuser.TbUserService;
import com.demo.util.SessionUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 周报
 */
public class WeekReportController extends Controller {

	@Inject
    WeekReportService service;
	
	public void index() {
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		List<Object> paramValue = new ArrayList<Object>();
		String condition = "";
		if(startTime != null){
			condition = condition + " and createTime>=?";
			paramValue.add(startTime);
		}
		if(endTime != null){
			condition = condition + " and createTime=<?";
			paramValue.add(endTime);
		}

		setAttr("page", service.paginate(getParaToInt(0, 1), 10,condition,paramValue));
		setAttr("startTime",startTime);
		setAttr("endTime",endTime);
		render("index.html");
	}

	public void add() {
		System.out.println("what ???");
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(WeekReportValidator.class)
	public void save() {
		TbWeekReport bean = getBean(TbWeekReport.class);

		Date now = new Date();
		bean.setCreateTime(now);
		bean.setUpdateTime(now);
		TbUser tbUser = SessionUtil.getTbUser(getSession());
		bean.setUserId(tbUser.getId());
		bean.setUserName(tbUser.getRealName());
		bean.save();
		redirect("/weekreport");
	}
	
	public void edit() {
		setAttr("tbWeekReport", service.findById(getParaToInt()));
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(WeekReportValidator.class)
	public void update() {
		TbWeekReport bean = getBean(TbWeekReport.class);
		bean.setUpdateTime(new Date());
		bean.update();
		redirect("/weekreport");
	}
	
	public void delete() {
		service.deleteById(getParaToInt());
		redirect("/weekreport");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
    public void download() throws IOException {
        HttpServletResponse response = getResponse();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
    }

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    /**
     * 文件上传
     * <p>1. 创建excel对应的实体对象 参照{@link UploadData}
     * <p>2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
     * <p>3. 直接读即可
     */
    public String upload() throws IOException {
        UploadFile file = getFile();
        EasyExcel.read(new FileInputStream(file.getFile()), UploadData.class, new UploadDataListener(null)).sheet().doRead();
        return "success";
    }
}


