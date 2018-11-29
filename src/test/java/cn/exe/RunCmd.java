package cn.exe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

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

    public static void main(String[] args) {
        String path = "E:\\hs\\software\\jfinal-3.5_demo_for_maven\\MsMd\\src\\test\\java\\cn\\exe\\run.bat";
        System.out.println(new Date());
        try {
            runCMDShow(path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(new Date());
    }
}
