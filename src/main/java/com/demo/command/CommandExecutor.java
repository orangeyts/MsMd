package com.demo.command;

import com.demo.common.model.TbProject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * command executor
 */
@Slf4j
public class CommandExecutor {


    public String execute(TbProject param) throws Exception {
        log.info("param: " + param);
        String script = param.getScript();

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(script.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        List<String> commands = new ArrayList<String>();
        String lineT = "";
        while ( (lineT = br.readLine()) != null ) {
            if(!lineT.trim().equals("")){
                System.out.println(lineT);
                commands.add(lineT);
            }
        }

        String command = "";
        int exitValue = -1;

        BufferedReader bufferedReader = null;
        try {
            // command process
            Process process = Runtime.getRuntime().exec(commands.toArray(new String[]{}));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.info(line);
            }

            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        if (exitValue == 0) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     *https://www.cnblogs.com/whatlonelytear/p/7885270.html
     * ProcessBuilder 用法
     * @throws IOException
     */
    public void execWindowCmd(Map<String, String> userEnv,File dir, String... command) throws IOException {

        ProcessBuilder pb = new ProcessBuilder();
        // 独立环境变量
        Map<String, String> env = pb.environment();
        env.putAll(userEnv);
        // 打印环境变量
        System.out.println(env);
        // 重定向错误输出流到正常输出流
        pb.redirectErrorStream(true);

        try {
            // 执行命令
            pb.directory(dir);
            pb.command(command);
            Process process1;
            // 启动进程
            process1 = pb.start();
            BufferedReader br1;
            br1 = new BufferedReader(new InputStreamReader(process1.getInputStream(), "gbk"));
            String line1 = null;
            while ((line1 = br1.readLine()) != null) {
                System.out.println(line1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        String s="1\r\n2\r\n3\r\n \r\nabd\r\n";
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        StringBuffer strbuf=new StringBuffer();
        while ( (line = br.readLine()) != null ) {
            if(!line.trim().equals("")){
                line="<br>"+line;//每行可以做加工
                strbuf.append(line+"\r\n");
            }
        }
        System.out.println(strbuf.toString());
    }
}
