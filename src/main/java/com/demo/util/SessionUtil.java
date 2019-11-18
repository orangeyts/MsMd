package com.demo.util;

import com.demo.common.model.TbUser;
import com.demo.constant.ConstantConfig;

import javax.servlet.http.HttpSession;


/**
 * 获取 登录用户id
 */
public class SessionUtil {
    public static TbUser getTbUser(HttpSession session) {
        Object sessionUid = session.getAttribute(ConstantConfig.SESSION_KEY);
        if (sessionUid != null)return (TbUser)sessionUid;
        return null;
    }
}
