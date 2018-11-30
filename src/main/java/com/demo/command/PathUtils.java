package com.demo.command;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class PathUtils {

    private static final Prop PROP= PropKit.use("a_little_config.txt");
    public static final String CI_HOME;
    static {
        CI_HOME = PROP.get("ci.home");
        System.out.println("主目录: " + CI_HOME);
    }
}
