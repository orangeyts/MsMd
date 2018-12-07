package enjoy;

import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.source.FileSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

/**
 * 模板变量
 */
@Slf4j
public class Enjon {

    @Test
    public void test(){
        Engine engine = Engine.create("myEngine");
        engine.setBaseTemplatePath("E:\\hs\\software\\jfinal-3.5_demo_for_maven\\MsMd\\src\\main\\resources\\scriptTemplate\\windows");
        engine.setDevMode(true);
        engine.setSourceFactory(new FileSourceFactory());

        Kv by = Kv.by("home", "E:\\freedom\\myci");
        by.set("projectName","spring-mvc-chat");
        by.set("scmUser","githubsync");
        by.set("scmPwd","githubsync1");

        by.set("scmPath","gitee.com/githubsync/spring-mvc-chat.git");
//        String s = engine.getTemplate("run.bat").renderToString(by);
//        log.info("out: {}",s);
        String file = by.get("home")+ File.separator+by.get("projectName")+".bat";
        engine.getTemplate("run.bat").render(by,file);
    }
}
