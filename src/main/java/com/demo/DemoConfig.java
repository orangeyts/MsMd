package com.demo;

import com.demo.blog.BlogController;
import com.demo.common.Slf4jLogFactory;
import com.demo.common.model.TbEnvConfig;
import com.demo.common.model._MappingKit;
import com.demo.constant.ConstantConfig;
import com.demo.env.TbEnvConfigController;
import com.demo.env.TbEnvConfigService;
import com.demo.index.IndexController;
import com.demo.io.HelloTioController;
import com.demo.io.client.HelloClientStarter;
import com.demo.io.server.HelloServerStarter;
import com.demo.project.ProjectController;
import com.demo.step.StepController;
import com.demo.util.FileUtils;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.FileKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jfinal.template.source.FileSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.StringUtil;

import java.io.File;
import java.util.Map;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * API引导式配置
 */
@Slf4j
public class DemoConfig extends JFinalConfig {
	
	/**
	 * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
	 * 
	 * 使用本方法启动过第一次以后，会在开发工具的 debug、run configuration 中自动生成
	 * 一条启动配置项，可对该自动生成的配置再继续添加更多的配置项，例如 Program arguments
	 * 可配置为：src/main/webapp 80 / 5
	 * 
	 */
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 801, "/", 5);
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用PropKit.get(...)获取值
		PropKit.use("a_little_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		
		// 支持 Controller、Interceptor 之中使用 @Inject 注入业务层，并且自动实现 AOP
		me.setInjectDependency(true);

		//slf4j日志框架
		me.setLogFactory(new Slf4jLogFactory());
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class, "/index");	// 第三个参数为该Controller的视图存放路径
		me.add("/blog", BlogController.class);			// 第三个参数省略时默认与第一个参数值相同，在此即为 "/blog"
		me.add("/project", ProjectController.class);
		me.add("/step", StepController.class);
		me.add("/env", TbEnvConfigController.class);

		me.add("/tio", HelloTioController.class);
	}
	
	public void configEngine(Engine me) {
		me.addSharedFunction("/common/_layout.html");
		me.addSharedFunction("/common/_paginate.html");
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置 druid 数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		me.add(druidPlugin);
		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(true);
		// 所有映射在 MappingKit 中自动化搞定
		_MappingKit.mapping(arp);
		me.add(arp);

		me.add(new HelloServerStarter());
		me.add(new HelloClientStarter());
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}

	static TbEnvConfigService service = Aop.get(TbEnvConfigService.class);

	@Override
	public void afterJFinalStart() {
		log.info("service: {}",service);
		Map<String, String> config = service.getConfig();
		String home = config.get(ConstantConfig.HOME);
		log.info("ci home: {}",home);
		if (StringUtil.isNotBlank(home)){
			File homePath = new File(home);
			if (!homePath.exists()){homePath.mkdirs();};
			String rootClassPath = PathKit.getRootClassPath();
			log.info("rootClassPath: {}",rootClassPath);
			String scriptTemplatePath = rootClassPath + File.separator + "scriptTemplate";
			FileUtils.directory(scriptTemplatePath,home);
			log.info("template copy complete");
		}else {
			log.error("un config: CI home key");
		}

		//git command path
		String gitRoot = config.get(ConstantConfig.GIT_ROOT);
		log.info("gitRoot: {}",gitRoot);
		//ci script template
		Engine engine = Engine.create(ConstantConfig.ENJOY_KEY);
		engine.setBaseTemplatePath(home);
		engine.setDevMode(true);
		engine.setSourceFactory(new FileSourceFactory());
	}
}
