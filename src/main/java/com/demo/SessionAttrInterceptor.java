package com.demo;

import com.demo.common.model.TbUser;
import com.demo.constant.ConstantConfig;
import com.demo.tbuser.TbUserService;
import com.demo.util.SessionUtil;
import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * https://blog.csdn.net/tczhoulan/article/details/48162489
 */
@Slf4j
public class SessionAttrInterceptor implements Interceptor {

    @Inject
    TbUserService userService;

    public void intercept(Invocation ai) {
        HttpServletRequest request = ai.getController().getRequest();
        //获得客户端发送请求的完整url
        String fullUrl = request.getRequestURL().toString();
        String url = request.getRequestURI();
        String referer = request.getHeader("Referer");
        log.info("url: [{}]   fullUrl: [{}]",url,fullUrl);
        if (url.endsWith("/toLogin")|| (url.endsWith("/login"))|| (url.endsWith("/toRegister"))|| (url.endsWith("/register"))){
            ai.invoke();
        }else{
            String uid = null;
            Cookie[] cookies = ai.getController().getRequest().getCookies();
            if (cookies != null && cookies.length > 0){
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(ConstantConfig.SESSION_KEY)){
                        uid = cookie.getValue();
                        log.info("user id ok : [{}]",uid);
                    }
                }
            }
            if (uid != null){
                HttpSession session = ai.getController().getSession();

                TbUser login = userService.findById(Integer.parseInt(uid));
                session.setAttribute(ConstantConfig.SESSION_KEY,login);

                ai.invoke();
            }else {
                log.error("session timeout");
                try {
                    ai.getController().redirect("/toLogin?url="+ URLEncoder.encode(fullUrl,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

