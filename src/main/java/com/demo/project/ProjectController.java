package com.demo.project;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
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
import com.demo.util.ExceptionUtil;
import com.demo.util.OSUtils;
import com.demo.util.ZipUtils;
import com.demo.util.edas.EdasAPI;
import com.demo.util.websocket.MyWebSocketClient;
import com.demo.util.websocket.model.CommandType;
import com.demo.util.websocket.model.JoinGroup;
import com.demo.util.websocket.model.Login;
import com.demo.vo.SubProjectVO;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import com.jfinal.upload.UploadFile;
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
	BuildService buildService;
	@Inject
	TbAccountService tbAccountService;
	@Inject
	TemplateService templateService;
	@Inject
	TbEnvConfigService tbEnvConfigService;

	public void index() {
		setAttr(modelKey+"Page", service.paginate(getParaToInt(0, 1), 10));
		setCookie("csrf",UUID.randomUUID().toString(),3600);
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
		TbProject byId = service.findById(getParaToInt());
		setAttr("tbProject", byId);
		setAttr("subProjects", JSON.parseArray(byId.getSubProjectJson(),SubProjectVO.class));
		List<TbAccount> tbAccountList = tbAccountService.getTbAccountList();
		setAttr("tbAccountList", tbAccountList);

		setCookie("csrf",UUID.randomUUID().toString(),3600);
		//
		List<TbProject> projectList = service.getList();
		setAttr("projectList", projectList);
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
		internalBuild(null);
	}

	public void toUploadbBuild() throws Exception {
		TbProject byId = service.findById(getParaToInt("projectId"));
		setAttr("tbProject", byId);
		setCookie("csrf",UUID.randomUUID().toString(),3600);
	}
	public void uploadBuild() throws Exception {
		UploadFile uploadFile = getFile("zip");
		internalBuild(uploadFile);
	}

	private void internalBuild(UploadFile uploadFile) throws Exception {
		Integer projectId = getParaToInt("projectId");
		String csrf = getCookie("csrf");
		if (StringUtils.isEmpty(csrf)){
			renderText("请从工程页面进入");
			return;
		}
		removeCookie("csrf");
		TbProject obj = service.findById(projectId);
		if (obj.getStatus() == 1){
			setAttr("errorMsg","工程正在运行中，请稍后重试");
			render(modelKey+".html");
			return;
		}

		TbBuild tbBuild = getTbBuild(obj);

		obj.updateProjectStatus(1,tbBuild.getId());

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					runScript(obj,tbBuild,uploadFile);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					obj.updateProjectStatus(0,tbBuild.getId());
					uploadFile.getFile().delete();
				}
			}
		}).start();

		Map<String, String> config = tbEnvConfigService.getConfig();
		String serverIp = config.get(ConstantConfig.SERVER_IP);

		if (PropKit.getBoolean("devMode", false)){
			serverIp = "localhost";
		}
		setAttr("serverIp",serverIp);
		setAttr("tbProject",obj);
		setAttr("tbBuild",tbBuild);
		render("/project/build.html");
	}

	public void buildOut(){
		Integer projectId = getParaToInt("projectId");
		TbProject project = service.findById(projectId);
		TbBuild tbBuild = buildService.findById(project.getBuildId());
		setAttr("tbBuild",tbBuild);

		Map<String, String> config = tbEnvConfigService.getConfig();
		String serverIp = config.get(ConstantConfig.SERVER_IP);

		setAttr("serverIp",serverIp);
		setAttr("uuid",UUID.randomUUID().toString());
		render("/project/buildOut.html");
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
	 * @param project
	 * @throws Exception
	 */
	private void runScript(TbProject project,TbBuild tbBuild,UploadFile uploadFile) throws Exception {


		try {
			Map<String, String> config = tbEnvConfigService.getConfig();
			String home = config.get(ConstantConfig.HOME);
			String gitRoot = config.get(ConstantConfig.GIT_ROOT);

			String script = project.getScript();
			Integer accountId = project.getAccountId();
			if (log.isInfoEnabled()){
				if (accountId == 0){
					log.info("非本地编译工程,仅仅执行远端 shell文件");
				}
			}
			Engine engine = Engine.use(ConstantConfig.ENJOY_KEY);

			Kv by = Kv.by("home", home);
			by.set("gitHome",gitRoot);
			by.set("projectName",project.getTitle());


			by.set("scmPath",project.getScmPath());
			String osExtion = ".sh";


			if (project.getType().intValue() == ConstantConfig.MAVEN){
				if (accountId != 0){
					tbBuild.appendOutput("开始构建git工程");
					TbAccount tbAccount = tbAccountService.getTbAccount(accountId);
					by.set("scmUser",tbAccount.getUserName());
					by.set("scmPwd",tbAccount.getPwd());
					log.info("账户信息: {}  {}",tbAccount.getUserName(),tbAccount.getPwd());

					String osCmdPrefix = "";
					if (ConstantOS.WINDOWS.equals(project.getOs())){
						osCmdPrefix = "cmd /c ";
						osExtion = ".bat";
					}else if (ConstantOS.LINUX.equals(project.getOs())){
						osExtion = ".sh";
					}
					String exeScriptFile = home + File.separator+by.get("projectName") + osExtion;
					engine.getTemplateByString(project.getScript()).render(by,exeScriptFile);

					List<String> commands = new ArrayList<String>();
					String lineT = osCmdPrefix + exeScriptFile;
					log.info("工程路径: {}",project.getScriptFilePath());
					File dir = new File(home);
					String[] split = lineT.split(" ");
					log.info("执行命令的值: {}",lineT);
					if (ConstantOS.LINUX.equals(project.getOs())){
						tbBuild.appendOutput("执行linux commandExecutor");
						if (OSUtils.LINUX){
							String fileName = by.get("projectName") + osExtion;
							String chmod = "chmod u+x "+ fileName;
							log.info("授权dir: {} chmod: {}",dir,chmod);
							new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,tbBuild,chmod.split(" "));
							chmod = "dos2unix -q " + fileName;
							log.info("转码 dir: {} chmod: {}",dir,chmod);
							new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,tbBuild,chmod.split(" "));
							log.info("授权.转码 .sh 成功");

							tbBuild.appendOutput("执行commandExecutor");
							new CommandExecutor().execWindowCmd(Collections.emptyMap(),dir,tbBuild,split);
						}else{
							//windows 测试环境 自己安装打包 命令无法执行
						}
					}

				}
			}else{
				//目前-maven工程才构建
			}



			if (project.getSshAccountId().intValue() != 0){
				tbBuild.appendOutput("开始构建ssh--------");

	//			String sshScriptFile =by.get("projectName") + "_ssh" + osExtion;
	//			Kv sshConfig = Kv.by("home", home);
	//			engine.getTemplateByString(project.getSshScript()).render(sshConfig, home + File.separator + sshScriptFile);

	//			sshClient.putFile(home, sshScriptFile,remotePath);
	//			sshClient.sendCmd("chmod u+x " + remotePath + "/" + sshScriptFile,tbBuild);
	//			sshClient.sendCmd("dos2unix " + remotePath + "/" + sshScriptFile,tbBuild);

				String subProjectJson = project.getSubProjectJson();
				if (!StringUtils.isEmpty(subProjectJson)){
					List<SubProjectVO> subProjectVOS = JSON.parseArray(subProjectJson, SubProjectVO.class);
					for(SubProjectVO subProject : subProjectVOS){
						String remotePath = subProject.getSshTargetDir();

						TbAccount sshTbAccount = tbAccountService.getTbAccount(subProject.getSshId());
						SSHClient sshClient = new SSHClient();
						sshClient.setHost(sshTbAccount.getIp()).setPort(22).setUsername(sshTbAccount.getUserName()).setPassword(sshTbAccount.getPwd());
						sshClient.login();
	//					zipAndUploadRemoteServer(project, home, by, sshClient,"v4-common-parent/v4-nuo-service-user","target/v4-nuo-service-user.jar",tbBuild);
						//压缩文件并且 上传到远程服务器
						zipAndUploadRemoteServer(project, home, by, sshClient,subProject,tbBuild,uploadFile);

						tbBuild.appendOutput("start 生成ssh脚本");
						String sshScriptFile = subProject.getProjectName() + "_ssh" + osExtion;
						by.set("subProjectName",subProject.getProjectName());
						engine.getTemplateByString(subProject.getSshScript()).render(by, home + File.separator + sshScriptFile);
						tbBuild.appendOutput("end 生成ssh脚本");
						remotePath = remotePath+"/" + subProject.getProjectName();
						sshClient.putFile(home, sshScriptFile,remotePath,tbBuild);
						sshClient.sendCmd("chmod u+x " + remotePath + "/" + sshScriptFile,tbBuild);
						sshClient.sendCmd("dos2unix " + remotePath + "/" + sshScriptFile,tbBuild);
						log.info("执行启动程序脚本 [{}]",remotePath + "/" + sshScriptFile);
						tbBuild.appendOutput("执行启动程序脚本 start");
						sshClient.sendCmd(remotePath + "/" + sshScriptFile,tbBuild);
						tbBuild.appendOutput("执行启动程序脚本 end");
					}
				}
	//			String sshCmdOut = sshClient.sendCmd(remotePath + "/" + sshScriptFile,tbBuild);
	//			String sshCmdOut = sshClient.sendShell(remotePath + "/" + sshScriptFile, tbBuild);
	//			log.info("sshCmdOut: {}",sshCmdOut);
			}else{
				log.warn("没有配置 ssh账户");
			}
			if (project.getEadsAccountId().intValue() != 0){
				tbBuild.appendOutput("开始构建edas上传");
				TbAccount edasTbAccount = tbAccountService.getTbAccount(project.getEadsAccountId());
				DefaultAcsClient defaultAcsClient = EdasAPI.getDefaultAcsClient(edasTbAccount.getRegionId(), edasTbAccount.getUserName(), edasTbAccount.getPwd());
				String all = EdasAPI.rollback(defaultAcsClient, project.getEdasAppId(), project.getEdasPackageVersion(), "all");
				tbBuild.appendOutput(all);
			}else{
				log.warn("没有配置 edas账户");
			}
		} catch (Exception e) {
			tbBuild.appendOutput(ExceptionUtil.getStackTrace(e));
			throw e;
		}
	}


	/**
	 * 获取本次构建对象-以及websocket输出对象
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private TbBuild getTbBuild(TbProject obj) throws Exception {
		TbBuild tbBuild = new TbBuild();
		tbBuild.setProjectId(obj.getId());
		tbBuild.setTriggerDesc("开始构建");
		tbBuild.save();

		String url = "ws://127.0.0.1:12345/ws";
		String loginMessage = JSON.toJSONString(new Login("build_" + tbBuild.getId()));
		MyWebSocketClient myWebSocketClient = MyWebSocketClient.connectServerAndLogin(url,loginMessage);
		String groupId = "build_group_" + tbBuild.getId();
		myWebSocketClient.send(JSON.toJSONString(new JoinGroup("build_" + tbBuild.getId(),groupId,CommandType.joinGroup)));
		tbBuild.setMyWebSocketClient(myWebSocketClient);
		tbBuild.setGroupId(groupId);
		return tbBuild;
	}


	/**
	 * 压缩文件上传
	 * @param obj
	 * @param home
	 * @param by
	 * @param sshClient
	 * @param subProject
	 * @throws Exception
	 */
	private void zipAndUploadRemoteServer(TbProject obj, String home, Kv by, SSHClient sshClient,SubProjectVO subProject, TbBuild tbBuild,UploadFile uploadFile) throws Exception {
		String projectPath = subProject.getProjectPath();
		String projectName = subProject.getProjectName();

		String zipDir = null;
		String zipFile = null;
		String zipFileName = "";
		if (subProject.getZip()){
			tbBuild.appendOutput("压缩文件...");
			zipFileName = projectName+".zip";

			if (obj.getUploadFile().intValue() == 1){
//				zipDir = home + File.separator+by.get("projectName") + File.separator + "dist";
//				zipFile = zipDir + File.separator + zipFileName;
//				ZipUtils.compressMultiFolder(zipDir,zipFile,subProject.getZipFolderOrFile().split(" "));
				//前端文件现在 还是手动打包
				zipDir = uploadFile.getUploadPath();
				zipFile = zipDir + File.separator + uploadFile.getFileName();
			}else if (obj.getType().intValue() == ConstantConfig.MAVEN){
				zipDir = home + File.separator+by.get("projectName") + File.separator + projectPath;
				zipFile = zipDir + File.separator + zipFileName;
				ZipUtils.compressMultiFolder(zipDir,zipFile,subProject.getZipFolderOrFile().split(" "));
			}
		}else{
			zipFileName = projectName+".jar";

			zipDir = home + File.separator+by.get("projectName") + File.separator + projectPath;
			zipFile = zipDir + File.separator + zipFileName;

		}

		if (zipFile != null){
			String out = String.format("ssh传送文件到服务器: dir [%s]  zipFileName [%s] ",zipDir,zipFileName);
			tbBuild.appendOutput(out);
			log.info(out);
			sshClient.putFile(zipDir,zipFileName,subProject.getSshTargetDir()+File.separator+subProject.getProjectName()+"/project_versions",tbBuild);
			tbBuild.appendOutput("ssh 上传成功");
			log.info("{}  上传成功",zipFileName);
		}
	}
}


