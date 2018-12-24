package com.demo;

import com.demo.constant.ConstantConfig;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;

/**
 * https://blog.csdn.net/tczhoulan/article/details/48162489
 */
@Slf4j
public class SessionAttrInterceptor implements Interceptor {

    public void intercept(Invocation ai) {
        String url = ai.getController().getRequest().getRequestURI();
        log.info("url: {}",url);
        if (url.endsWith("/toLogin")|| (url.endsWith("/login"))){
            ai.invoke();
        }else{
            HttpSession session = ai.getController().getSession();
            String nickname = (String) session.getAttribute(ConstantConfig.SESSION_KEY);
            if(nickname != null) {
                ai.invoke();
            }else {
                log.error("session timeout");
                ai.getController().redirect("/toLogin");
            }
        }
    }
}

