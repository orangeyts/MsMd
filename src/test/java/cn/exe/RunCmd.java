package cn.exe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * 原理：java的Runtime.getRuntime().exec(commandText)可以调用执行cmd指令。
 * <p>
 * cmd /c dir 是执行完dir命令后关闭命令窗口。
 * cmd /k dir 是执行完dir命令后不关闭命令窗口。
 * <p>
 * cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。
 * cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。
 * 注：增加了start，就会打开新窗口，可以用cmd /?查看帮助信息。
 *
 * https://www.cnblogs.com/jing1617/p/6430141.html
 */
public class RunCmd {

    public static void runCMD(String path) throws Exception {
        Process p = Runtime.getRuntime().exec("cmd /c cmd.exe /c " + path + " exit");
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLine = br.readLine();
        while (readLine != null) {
            readLine = br.readLine();
            System.out.println(readLine);
        }
        if (br != null) {
            br.close();
        }
        p.destroy();
        p = null;
    }

    public static void runCMDShow(String path) throws Exception {
        Process p = Runtime.getRuntime().exec("cmd.exe /c " + path + "");
//        Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe /c " + path + " exit");
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLine = br.readLine();
        while (readLine != null) {
            readLine = br.readLine();
            System.out.println(readLine);
        }
        if (br != null) {
            br.close();
        }
        p.destroy();
        p = null;
    }

    /**
     *https://www.cnblogs.com/whatlonelytear/p/7885270.html
     * ProcessBuilder 用法
     * @throws IOException
     */
    public static void execWindowCmd(Map<String, String> userEnv,String... command) throws IOException {

        ProcessBuilder pb = new ProcessBuilder();// 命令
        Map<String, String> env = pb.environment();// 独立环境变量
        env.putAll(userEnv);
        System.out.println(env);// 打印环境变量
        pb.redirectErrorStream(true);// 重定向错误输出流到正常输出流

        try {
//            pb.directory(new File("d://freedom"));// 设置目录freedom
//            pb.command("cmd", "/c", "dir");// 执行命令
            pb.command(command);// 执行命令
            Process process1;
            process1 = pb.start();// 启动进程
            BufferedReader br1;
            br1 = new BufferedReader(new InputStreamReader(process1.getInputStream(), "gbk"));
            String line1 = null;
            while ((line1 = br1.readLine()) != null) {
                System.out.println(line1);
            }

            pb.directory(new File("d://gitee"));// 设置目录test2
            pb.command("cmd", "/c", "dir", ">>", "test1.log");// 执行命令,把结果输出到test1.log
            Process process2 = pb.start();// 启动进程
            BufferedReader br2 = new BufferedReader(new InputStreamReader(process2.getInputStream(), "gbk"));
            String line2 = null;
            while ((line2 = br2.readLine()) != null) {//因为结果输出到了文件,所以本处无信息返回
                System.out.println(line2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        String path = "E:\\hs\\software\\jfinal-3.5_demo_for_maven\\MsMd\\src\\test\\java\\cn\\exe\\run.bat";
        System.out.println(new Date());
        try {
//            runCMDShow(path);
            execWindowCmd(Collections.emptyMap(),path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(new Date());
    }
}
