package com.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyBatisFormatter {
    public MyBatisFormatter() {
    }

    public static String format(String sql) throws IOException {
        BufferedReader bis = new BufferedReader(new StringReader(sql));
        String sqlText = bis.readLine();
        String sqlParam = bis.readLine();
        String sqlNoParams = sqlText.split("Preparing:")[1];
        String[] paramValues = sqlParam.split("Parameters:")[1].split(",");

        List<Object> paramObjs = new ArrayList();
        for (String paramValue : paramValues) {
            log.info(paramValue);
            String param = paramValue.split("\\(")[0];
            if (paramValue.contains("String")) {
                param = "\"" + param + "\"";
            } else {
               /* log.info("非字符串参数 按照实际类型,格式化输出");
                String regex = "\\(([^}]*)\\)";
                Pattern compile = Pattern.compile(regex);
                Matcher matcher = compile.matcher(paramValue);
                while(matcher.find()){
                    String group = matcher.group(1);
                    System.out.println("------------------------Group 0:"+matcher.group(1));//得到第0组——整个匹配
                }*/
            }
            paramObjs.add(param);
        }
        boolean containChar = sqlNoParams.contains("%");
        if (containChar) {
            //格式化带有% 报异常 java.util.UnknownFormatConversionException: Conversion = 'Y'，先替换为其他字符，格式化好后，再换回来
            sqlNoParams = sqlNoParams.replace("%", "@@");
        }

        String replaceSqlNoParams = sqlNoParams.replace("?", "%s");
        log.info(replaceSqlNoParams);
        log.info(paramObjs.toString());

//        System.out.println(String.format("大家好，我叫：%s", "小明"));
        String formatedSQL = String.format(replaceSqlNoParams, paramObjs.toArray());
        if (containChar) {
            formatedSQL = formatedSQL.replace("@@", "%");
        }
        log.info(formatedSQL);
        return formatedSQL;
//        sqlText.
    }
}