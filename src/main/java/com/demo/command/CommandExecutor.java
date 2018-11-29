package com.demo.command;

import com.demo.common.model.TbProject;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * command executor
 */
public class CommandExecutor {

    Logger logger = Logger.getLogger(CommandExecutor.class);
    
    public String execute(TbProject param) throws Exception {
        logger.info("param: " + param);
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
                logger.info(line);
            }

            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            logger.error(e);
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
