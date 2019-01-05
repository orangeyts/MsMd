package com.demo.project;

import com.demo.account.TbAccountService;
import com.demo.command.CommandExecutor;
import com.demo.command.PathUtils;
import com.demo.command.linux.SSHClient;
import com.demo.common.model.TbAccount;
import com.demo.common.model.TbBuild;
import com.demo.common.model.TbProject;
import com.demo.common.model.TbTemplate;
import com.demo.constant.ConstantConfig;
import com.demo.constant.ConstantOS;
import com.demo.env.TbEnvConfigService;
import com.demo.template.TemplateService;
import com.demo.util.ZipUtils;
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
	TemplateService templateService;
	@Inject
	TbEnvConfigService tbEnvConfigService;

	public void index() {
		setAttr(modelKey+"Page", service.paginate(getParaToInt(0, 1), 10));
		render(modelKey+".html");
	}
	
	public void add() {
		List<TbAccount> tbAccountList = tbAccountService.getTbAccountList();
		setAttr("tbAccountList", tbAccountList);
		List<TbTemplate> templateList = templateService.getList();
		setAttr("templateList", templateList);
	}
	public void getTemplate() {
		TbTemplate template = templateService.getById(getParaToInt("id", 1),getPara("os", ConstantOS.LINUX));
		renderJson(template);
	}

	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	public void save() {
		TbProject bean = getBean(TbProject.class);
		String scmPath = bean.getScmPath();
		String scmPathRelace = scmPath.replace("https://","");
		bean.setScmPath(scmPathRelace);
		service.checkRepeatName(bean);
		if (bean.getId() == null){
			bean.setCreateTime(new Date());
			bean.setUpdateTime(bean.getCreateTime());
			bean.save();
		}else{
			bean.setUpdateTime(new Date());
			bean.update();
		}

		redirect("/"+modelKey);
	}
	
	public void edit() {
		setAttr("tbProject", service.findById(getParaToInt()));
		List<TbAccount> tbAccountList = tbAccountService.getTbAccountList();
		setAttr("tbAccountList", tbAccountList);
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
	public void copy() throws Exception {
		Integer projectId = getParaToInt("projectId");
		TbProject obj = service.findById(projectId);
		obj.setId(null);
		obj.setTitle(UUID.randomUUID().toString());
		obj.save();
		redirect("/"+modelKey);
	}

	/**
	 * 1 第一次clone 2 第二次 pull
	 * @param obj
	 * @throws Exception
	 */
	private void runScript(TbProject obj) throws Exception {
		Map<String, String> config = tbEnvConfigService.getConfig();
		String home = config.get(ConstantConfig.HOME);
		String gitRoot = config.get(ConstantConfig.GIT_ROOT);


		String script = obj.getScript();
		Integer accountId = obj.getAccountId();
		TbAccount tbAccount = tbAccountService.getTbAccount(accountId);
		log.info("账户信息: {}  {}",tbAccount.getUserName(),tbAccount.getPwd());

		Engine engine = Engine.use(ConstantConfig.ENJOY_KEY);

		Kv by = Kv.by("home", home);
		by.set("gitHome",gitRoot);
		by.set("projectName",obj.getTitle());
		by.set("scmUser",tbAccount.getUserName());
		by.set("scmPwd",tbAccount.getPwd());

		by.set("scmPath",obj.getScmPath());
//        String s = engine.getTemplate("run.bat").renderToString(by);
//        log.info("out: {}",s);
		String osExtion = ".sh";
		String osCmdPrefix = "";
		if (ConstantOS.WINDOWS.equals(obj.getOs())){
			osCmdPrefix = "cmd /c ";
			osExtion = ".bat";
		}else if (ConstantOS.LINUX.equals(obj.getOs())){
			osExtion = ".sh";
		}
		String exeScriptFile = home + File.separator+by.get("projectName") + osExtion;
		engine.getTemplateByString(obj.getScript()).render(by,exeScriptFile);
//		engine.getTemplate(obj.getOs() + File.separator +"run" + osExtion).render(by,exeScriptFile);

		List<String> commands = new ArrayList<String>();
		String lineT = osCmdPrefix + exeScriptFile;
		log.info("工程路径: {}",obj.getScriptFilePath());
		File dir = new File(home);
		String[] split = lineT.split(" ");
		log.info("执行命令的值: {}",lineT);
		if (ConstantOS.LINUX.equals(obj.getOs())){
			String fileName = by.get("projectName") + osExtion;
			String chmod = "chmod u+x "+ fileName;
			log.info("授权dir: {} chmod: {}",dir,chmod);
			new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,chmod.split(" "));
            chmod = "dos2unix -q " + fileName;
            log.info("转码 dir: {} chmod: {}",dir,chmod);
            new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,chmod.split(" "));
			log.info("授权.转码 .sh 成功");
		}
		new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,split);

		if (obj.getSshAccountId().intValue() != 0){
			TbAccount sshTbAccount = tbAccountService.getTbAccount(obj.getSshAccountId());
			SSHClient sshClient = new SSHClient();
			sshClient.setHost(sshTbAccount.getIp()).setPort(22).setUsername(sshTbAccount.getUserName()).setPassword(sshTbAccount.getPwd());
			sshClient.login();

			String sshScriptFile =by.get("projectName") + "_ssh" + osExtion;
			Kv sshConfig = Kv.by("home", home);
			engine.getTemplateByString(obj.getSshScript()).render(sshConfig, home + File.separator + sshScriptFile);
			String remotePath = "/data";
			sshClient.putFile(home, sshScriptFile,remotePath);
			sshClient.sendCmd("chmod u+x " + remotePath + "/" + sshScriptFile);
			sshClient.sendCmd("dos2unix " + remotePath + "/" + sshScriptFile);


			zipAndUploadRemoteServer(obj, home, by, sshClient);
			String sshCmdOut = sshClient.sendCmd(remotePath + "/" + sshScriptFile);
			log.info("sshCmdOut: {}",sshCmdOut);
		}else{
			log.warn("没有配置 ssh账户");
		}

		/*TbBuild tbBuild = new TbBuild();
		tbBuild.setCreateTime(new Date());
		tbBuild.setProjectId(obj.getId());
		tbBuild.setTriggerDesc("desc");
		boolean save = tbBuild.save();

		String absPath = PathUtils.CI_HOME + File.separator + obj.getId() + File.separator + tbBuild.getId();
		*/
//		File dir = new File(absPath);

	}

	private void zipAndUploadRemoteServer(TbProject obj, String home, Kv by, SSHClient sshClient) throws Exception {
		String zipDir = null;
		String zipFile = null;
		String zipFileName = "dist.zip";
		if (obj.getType().intValue() == ConstantConfig.VUE){
			zipDir = home + File.separator+by.get("projectName") + File.separator + "dist";
			zipFile = zipDir + File.separator + zipFileName;
			ZipUtils.compress(zipDir,zipFile);
		}
		if (zipFile != null){
			sshClient.putFile(zipDir,zipFileName,"/data");
			log.info("{}  上传成功",zipFileName);
		}
	}
}


