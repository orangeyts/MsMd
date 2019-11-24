package com.demo.weekreport;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.demo.blog.BlogInterceptor;
import com.demo.blog.BlogValidator;
import com.demo.common.model.Blog;
import com.demo.common.model.TbUser;
import com.demo.common.model.TbWeekReport;
import com.demo.tbuser.TbUserService;
import com.demo.util.SessionUtil;
import com.demo.vo.TbWeekReportVO;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import org.tio.utils.hutool.BeanUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 周报
 */
public class WeekReportController extends Controller {

	@Inject
    WeekReportService service;
	
	public void index() {
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		Integer pageNumber = getParaToInt("page", 1);
		Page<TbWeekReport> paginate = getTbWeekReportPage(startTime, endTime, pageNumber,20);
		setAttr("page", paginate);
		setAttr("startTime",startTime);
		setAttr("endTime",endTime);
		render("index.html");
	}


	/**
	 * 导出的时候 用同样的处理逻辑 只是pageSize搞大一点就行了
	 * @param startTime
	 * @param endTime
	 * @param pageNumber
	 * @return
	 */
	private Page<TbWeekReport> getTbWeekReportPage(String startTime, String endTime, Integer pageNumber, Integer pageSize) {
		List<Object> paramValue = new ArrayList<Object>();
		String condition = "";
		if(!StringUtils.isEmpty(startTime)){
			condition = condition + " and reportDate>=?";
			paramValue.add(startTime);
		}
		if(!StringUtils.isEmpty(endTime)){
			condition = condition + " and reportDate<=?";
			paramValue.add(endTime);
		}
		return service.paginate(pageNumber, pageSize, condition, paramValue);
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

	/**
	 * JDK 日期函数  https://mp.weixin.qq.com/s/az40Pa0BAXepCR4aiLan-g
	 * @throws IOException
	 */
    public void download() throws IOException {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String format = localDate.format(dateTimeFormatter);
		String sheetName = format +"_平台组-工作周报";

        HttpServletResponse response = getResponse();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(sheetName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		Integer pageNumber = getParaToInt("page", 1);
		Page<TbWeekReport> paginate = getTbWeekReportPage(startTime, endTime, pageNumber,100);
		List<TbWeekReport> list = paginate.getList();

		//转换到 VO list
		List<TbWeekReportVO> result = list.stream().map(temp -> {
			TbWeekReportVO obj = new TbWeekReportVO();
			obj.setUserName(temp.getUserName());
			obj.setContent(temp.getContent());
			obj.setSummary(temp.getSummary());
			obj.setReportDate(temp.getReportDate());
			return obj;
		}).collect(Collectors.toList());

		EasyExcel.write(response.getOutputStream(), TbWeekReportVO.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet(sheetName).doWrite(result);
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


