package com.demo.project;

import com.demo.account.TbAccountService;
import com.demo.command.CommandExecutor;
import com.demo.command.PathUtils;
import com.demo.common.model.TbAccount;
import com.demo.common.model.TbBuild;
import com.demo.common.model.TbProject;
import com.demo.constant.ConstantConfig;
import com.demo.env.TbEnvConfigService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * StepController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Slf4j
public class ProjectController extends Controller {

	private String modelKey = "project";
	@Inject
	ProjectService service;
	@Inject
	TbAccountService tbAccountService;
	@Inject
	TbEnvConfigService tbEnvConfigService;

	public void index() {
		setAttr(modelKey+"Page", service.paginate(getParaToInt(0, 1), 10));
		render(modelKey+".html");
	}
	
	public void add() {
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void save() {
		getBean(TbProject.class).save();
		redirect("/"+modelKey);
	}
	
	public void edit() {
		setAttr("tbProject", service.findById(getParaToInt()));
	}
	
	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void update() {
		TbProject bean = getBean(TbProject.class);
		bean.update();
		redirect("/"+modelKey);
	}
	
	public void delete() {
		service.deleteById(getParaToInt());
		redirect("/"+modelKey);
	}
	public void build() throws Exception {
		Integer projectId = getParaToInt("projectId");
		TbProject obj = service.findById(projectId);
		runScript(obj);

		setAttr("tbProject",obj);
		render("/project/build.html");
	}

	/**
	 * 1 第一次clone 2 第二次 pull
	 * @param obj
	 * @throws Exception
	 */
	private void runScript(TbProject obj) throws Exception {
		Map<String, String> config = tbEnvConfigService.getConfig();
		String home = config.get(ConstantConfig.HOME);


		String script = obj.getScript();
		Integer accountId = obj.getAccountId();
		TbAccount tbAccount = tbAccountService.getTbAccount(accountId);
		log.info("账户信息: {}  {}",tbAccount.getUserName(),tbAccount.getPwd());

		Engine engine = Engine.use(ConstantConfig.ENJOY_KEY);

		Kv by = Kv.by("home", home);
		by.set("projectName","spring-mvc-chat");
		by.set("scmUser","githubsync");
		by.set("scmPwd","githubsync1");

		by.set("scmPath","gitee.com/githubsync/spring-mvc-chat.git");
//        String s = engine.getTemplate("run.bat").renderToString(by);
//        log.info("out: {}",s);
		String exeScriptFile = home + File.separator+by.get("projectName") + ".bat";
		engine.getTemplate("windows" + File.separator +"run.bat").render(by,exeScriptFile);

		List<String> commands = new ArrayList<String>();
		String lineT = "cmd /c "+exeScriptFile;
		log.info("工程路径: {}",obj.getScriptFilePath());
		File dir = new File(home);
		String[] split = lineT.split(" ");
		new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,split);
		/*TbBuild tbBuild = new TbBuild();
		tbBuild.setCreateTime(new Date());
		tbBuild.setProjectId(obj.getId());
		tbBuild.setTriggerDesc("desc");
		boolean save = tbBuild.save();

		String absPath = PathUtils.CI_HOME + File.separator + obj.getId() + File.separator + tbBuild.getId();
		*/
//		File dir = new File(absPath);

	}
}


